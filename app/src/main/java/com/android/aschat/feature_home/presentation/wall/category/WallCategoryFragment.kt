package com.android.aschat.feature_home.presentation.wall.category

import android.content.Context
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.android.aschat.R
import com.android.aschat.databinding.HomeWallTagFragmentBinding
import com.android.aschat.feature_home.domain.model.wall.subtag.HostData
import com.android.aschat.feature_home.domain.rv.ListState
import com.android.aschat.feature_home.presentation.HomeEvents
import com.android.aschat.feature_home.presentation.HomeViewModel
import com.android.aschat.feature_login.domain.model.strategy.BroadcasterWallTag
import com.android.aschat.util.DensityUtil
import com.android.aschat.util.LogUtil
import com.drake.brv.BindingAdapter
import com.drake.brv.listener.ItemDifferCallback
import com.drake.brv.utils.grid
import com.drake.brv.utils.setup
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WallCategoryFragment(private val broadcasterWallTag: BroadcasterWallTag): Fragment() {

    private lateinit var mBinding: HomeWallTagFragmentBinding

    private val mTabHolders: MutableList<SubTagHolder> = mutableListOf()

    /**
     * 这是每一个category的viewmodel
     */
    private val mViewModel: WallCategoryViewModel by viewModels()

    /**
     * 这是HomeViewModel
     */
    private val mViewModel2: HomeViewModel by activityViewModels()

    // rv的adapter
    private lateinit var mRvAdapter: BindingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = HomeWallTagFragmentBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        initWidget()
    }

    fun initBinding() {

    }

    fun initWidget() {
        // initViewModel
        mViewModel.onEvent(SubTagEvents.InitSubTags(broadcasterWallTag))
        // 设置监听
        mBinding.wallTagTablayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab == null) return
                setTabSelected(mTabHolders[tab.position])
                // 向viewmodel发送更改tag的事件
                mViewModel.onEvent(SubTagEvents.ChangeTab(broadcasterWallTag.subTagList[tab.position]))
                // 切换tag回到顶部
                mBinding.wallTagFragmentRv.scrollToPosition(0)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                if (tab == null) return
                setTabUnSelected(mTabHolders[tab.position])
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
        // 初始化，先设置监听可以防止一开始没有选择第一个
        broadcasterWallTag.subTagList.forEach {
            val tab = mBinding.wallTagTablayout.newTab().apply {
                setCustomView(R.layout.home_wall_tab)
                val holder = SubTagHolder(this.customView!!)
                setTabUnSelected(holder, it)
                mTabHolders.add(holder)
            }
            mBinding.wallTagTablayout.addTab(tab)
        }

        // 开始设置rv todo Jeck 刷新状态机制
        mRvAdapter = mBinding.wallTagFragmentRv.grid(
            spanCount = 2
        ).apply {
           this.addItemDecoration(
               MainSpaceDecoration(requireContext(), 6)
           )
        }.setup {
            addType<HostData>(R.layout.home_wall_host_item)
            // 设置数据去重判断逻辑
            itemDifferCallback = object :ItemDifferCallback {
                override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                    val old = oldItem as HostData
                    val new = newItem as HostData
                    return old == new
                }

                override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
                    val old = oldItem as HostData
                    val new = newItem as HostData
                    return old.userId == new.userId
                }
            }
            // 设置点击事件
            onClick(R.id.home_wall_host_item) {
                // 把点击的对象传给viewmodel
                val hostData = this.getModel() as HostData
                mViewModel2.onEvent(HomeEvents.ClickHost(hostData))
            }
        }
        mBinding.wallTagFragmentPage.apply {
            // 可以开始预加载的位置：倒数第十条数据开始”执行onLoadMore逻辑“
            preloadIndex = 10

            onRefresh {
                LogUtil.d("onRefresh")
                mViewModel.onEvent(SubTagEvents.WantRefresh)
            }

            onLoadMore {
                LogUtil.d("loadmore")
                mViewModel.onEvent(SubTagEvents.WantMore)
            }
        }
        // 设置对列表的监听
        mViewModel.nowTagHosts.observe(viewLifecycleOwner) {
            // 更改当前rv的数据
            LogUtil.d("更改wall category rv的数据")
            when (mViewModel.mListState) {
                ListState.REPLACE -> {
//                    PageRefreshLayout 支持自动分页加载, 自动分页不需要你调用rv.models函数去设置数据, 使用addData即可
                    mRvAdapter.models = mutableListOf()
//                    mBinding.wallTagFragmentPage.addData(it)
                    mRvAdapter.setDifferModels(it, false)
                    // 要自己结束状态
                    mBinding.wallTagFragmentPage.finish()
                }
                ListState.ADD -> {
//                    mBinding.wallTagFragmentPage.addData(it)
                    mRvAdapter.setDifferModels(it, false)
                    mBinding.wallTagFragmentPage.finish()
                }
            }
        }
    }

    private fun setTabUnSelected(tabHolder: SubTagHolder, subTag: String = "") {
        tabHolder.apply {
            setTextColor(requireContext(), R.color.tab_gray)
            if (subTag.isNotEmpty())
                // 后面在进入非选择时不用重新赋值文本
                setTextContent(subTag)
            setTextSize(12f)
            setTextWeight(Typeface.NORMAL)
        }
    }
    private fun setTabSelected(tabHolder: SubTagHolder) {
        tabHolder.apply {
            setTextColor(requireContext(), R.color.purple)
            setTextWeight(Typeface.BOLD)
        }
    }
}

class MainSpaceDecoration(context: Context, space2Dp: Int) : RecyclerView.ItemDecoration() {

    //dp转px
    val delta: Int = DensityUtil.dip2px(space2Dp.toFloat(), context)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.top = delta
        if (parent.getChildAdapterPosition(view) % 2 == 0) {
            outRect.left = delta
            outRect.right = delta
        } else if (parent.getChildAdapterPosition(view) % 2 == 1) {
            outRect.left = 0
            outRect.right = delta
        }
    }
}

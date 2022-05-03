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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.aschat.R
import com.android.aschat.common.Constants
import com.android.aschat.databinding.HomeWallTagFragmentBinding
import com.android.aschat.feature_home.domain.model.wall.subtag.HostData
import com.android.aschat.feature_home.domain.rv.ListState
import com.android.aschat.feature_home.presentation.HomeEvents
import com.android.aschat.feature_home.presentation.HomeViewModel
import com.android.aschat.feature_login.domain.model.strategy.BroadcasterWallTag
import com.android.aschat.util.DensityUtil
import com.android.aschat.util.LogUtil
import com.android.aschat.util.equilibriumAssignmentOfGrid
import com.android.aschat.util.setHostStatus
import com.drake.brv.BindingAdapter
import com.drake.brv.listener.ItemDifferCallback
import com.drake.brv.utils.grid
import com.drake.brv.utils.setup
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.Executors

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

    // 定时刷新的定时器
    private val mTimer by lazy {
        Timer()
    }

    private lateinit var mTimerTask: TimerTask

    private lateinit var mLm: GridLayoutManager

    /**
     * 判断是否第一次resume的标志位
     */
    private var mFirstTimeResumeFlag = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        LogUtil.d("$this onCreateView")
        mBinding = HomeWallTagFragmentBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LogUtil.d("$this onViewCreated")
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
            this.addItemDecoration(object :RecyclerView.ItemDecoration(){
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    equilibriumAssignmentOfGrid(DensityUtil.dip2px(3f, requireContext()), outRect, view, parent)
                }
            })
            // 赋值layoutManager字段
            mLm = layoutManager!! as GridLayoutManager
        }.setup {
            addType<HostData>(R.layout.home_wall_host_item)
            // 设置数据去重判断逻辑
            itemDifferCallback = object :ItemDifferCallback {
                override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                    val old = oldItem as HostData
                    val new = newItem as HostData
//                    if (old == new) {
//                        LogUtil.d("相同${old.userId}")
//                    }else {
//                        LogUtil.d("不相同${old.userId}/${new.userId}")
//                    }
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

            // 避免刷新是闪烁
            onPayload {
                LogUtil.d("onPayload ${(this.getModel() as HostData).userId}")
                setHostStatus(this.findView(R.id.host_status), (this.getModel() as HostData).status)
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
        // 监听rv的滑动事件
        mBinding.wallTagFragmentRv.addOnScrollListener(object :RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                LogUtil.d("新状态 $newState")
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        // NOTE 1)列表停止滑动，滑动刷新
                        LogUtil.d("滑动刷新")
                        refreshHostOnScreen()
                    }
                }
            }
        })
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
//        launchTimerTask()
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

    override fun onStart() {
        super.onStart()
        LogUtil.d("$this onStart")
    }

    /**
     * NOTE viewpager的fragment互相切换时，两个fragment的pause和resume互相调用
     */
    override fun onResume() {
        super.onResume()
        LogUtil.d("$this onResume")
        if (mFirstTimeResumeFlag) {
            mFirstTimeResumeFlag = false
            // 启动定时器 NOTE 3)定时刷新
            launchTimerTask()
        }else {
            // NOTE 2)切换刷新
            // 不是第一次resume，立即刷新当前可见主播
            LogUtil.d("切换刷新")
            refreshHostOnScreen()
            // 启动定时器 NOTE 3)定时刷新
            launchTimerTask()
        }
    }

    /**
     * NOTE viewpager的fragment互相切换时，两个fragment的pause和resume互相调用
     */
    override fun onPause() {
        super.onPause()
        LogUtil.d("$this onPause")
        // 取消定时器
        cancelTimerTask()
    }

    override fun onStop() {
        super.onStop()
        LogUtil.d("$this onStop")
    }
    override fun onDestroy() {
        super.onDestroy()
        LogUtil.d("$this onDestroy")
    }

    /**
     * 返回一个任务
     */
    private fun getTimerTask(): TimerTask{
        return object :TimerTask() {
            override fun run() {
                LogUtil.d("定时刷新")
                refreshHostOnScreen()
            }
        }
    }

    /**
     * 刷新当前可见主播的数据，时间戳判定在viewmodel做
     *
     * NOTE 刷新机制和viewmodel交互的接口
     */
    private fun refreshHostOnScreen() {
        if (!::mLm.isInitialized) return
        // NOTE rv的index和viewmodel数据的index可以对应，我选择更新viewmodel中的数据而不是直接更新rv
        val firstVisibleIndex = mLm.findFirstVisibleItemPosition()
        val lastVisibleIndex = mLm.findLastVisibleItemPosition()
        LogUtil.d("firstIndexRefresh: $firstVisibleIndex  lastIndexRefresh: $lastVisibleIndex")
        if (!checkIndexInBounds(firstVisibleIndex, lastVisibleIndex)) return
        // NOTE 我只管把可见item的index范围传给viewmodel，更新由viewmodel来做
        mViewModel.onEvent(SubTagEvents.RefreshHost(firstVisibleIndex, lastVisibleIndex) {
            // 切换到主线程去执行
            lifecycleScope.launch(Dispatchers.Main) {
                // 使用payload字段去局部更新status
                mRvAdapter.notifyItemRangeChanged(firstVisibleIndex, lastVisibleIndex - firstVisibleIndex + 1, 0)
            }
        })
    }

    /**
     * 启动定时任务
     */
    private fun launchTimerTask() {
        // NOTE 新建一个任务对象
        mTimerTask = getTimerTask()
        mTimer.schedule(mTimerTask, Constants.Host_Refresh_Stamp, Constants.Host_Refresh_Stamp)
    }

    /**
     * 取消定时任务
     */
    private fun cancelTimerTask() {
        if(!::mTimerTask.isInitialized) return
        try {
            mTimerTask.cancel()
        }catch (e: Exception) {
            e.printStackTrace()
            LogUtil.e("定时任务关闭失败")
        }
    }

    /**
     * 判断下标是否合法
     */
    private fun checkIndexInBounds(firstIndex: Int, lastIndex: Int): Boolean {
        return firstIndex >= 0 && lastIndex >= firstIndex
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

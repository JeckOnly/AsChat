package com.android.aschat.feature_home.presentation.message.followed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.aschat.R
import com.android.aschat.databinding.HomeMessageFollowedFragmentBinding
import com.android.aschat.feature_home.domain.model.follow.FollowFriend
import com.android.aschat.feature_home.domain.rv.ListState
import com.android.aschat.feature_home.presentation.HomeEvents
import com.android.aschat.feature_home.presentation.HomeViewModel
import com.android.aschat.util.LogUtil
import com.drake.brv.BindingAdapter
import com.drake.brv.listener.ItemDifferCallback
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FollowedFragment: Fragment() {

    private lateinit var mBinding: HomeMessageFollowedFragmentBinding
    private val mViewModel: HomeViewModel by activityViewModels()

    // rv的adapter
    private lateinit var mRvAdapter: BindingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = HomeMessageFollowedFragmentBinding.inflate(inflater)
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
        mViewModel.apply {
            onEvent(HomeEvents.FollowWantInit)
        }
        mBinding.apply {
            mRvAdapter = this.followRv.linear().setup {
                addType<FollowFriend>(R.layout.home_message_followed_item)
                // 设置数据去重判断逻辑
                itemDifferCallback = object : ItemDifferCallback {
                    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                        val old = oldItem as FollowFriend
                        val new = newItem as FollowFriend
                        return old == new
                    }

                    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
                        val old = oldItem as FollowFriend
                        val new = newItem as FollowFriend
                        return old.userId == new.userId
                    }

                    override fun getChangePayload(oldItem: Any, newItem: Any): Any {
                        return true
                    }
                }
                // 设置点击事件
                onClick(R.id.home_message_followed_item) {
                    // 把点击的对象传给viewmodel
                    val friend = this.getModel() as FollowFriend
                    mViewModel.onEvent(HomeEvents.ClickFriend(friend))
                }
            }
            followPage.apply {
                // 可以开始预加载的位置：倒数第十条数据开始”执行onLoadMore逻辑“
                preloadIndex = 10

                onRefresh {
                    LogUtil.d("onRefresh")
                    mViewModel.onEvent(HomeEvents.FollowWantRefresh)
                }

                onLoadMore {
                    LogUtil.d("loadmore")
                    mViewModel.onEvent(HomeEvents.FollowWantMore)
                }
            }
        }

        mViewModel.friends.observe(viewLifecycleOwner) {
            // 更改当前rv的数据
            LogUtil.d("更改follow rv的数据 ${it.size}")
            when (mViewModel.followListState) {
                ListState.REPLACE -> {
                    //PageRefreshLayout 支持自动分页加载, 自动分页不需要你调用rv.models函数去设置数据, 使用addData即可
                    mRvAdapter.models = mutableListOf()
//                    mBinding.followPage.addData(it)
                    mRvAdapter.setDifferModels(newModels = it, detectMoves = false)
                    mBinding.followPage.finish()
                }
                ListState.ADD -> {
//                    mBinding.followPage.addData(it)
                    mRvAdapter.setDifferModels(newModels = it, detectMoves = false)
                    mBinding.followPage.finish()
                }
            }
        }
    }
}
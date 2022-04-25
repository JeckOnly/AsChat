package com.android.aschat.feature_home.presentation.coin

import android.graphics.Rect
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.android.aschat.R
import com.android.aschat.databinding.HomeCoinFragmentBinding
import com.android.aschat.feature_home.presentation.HomeEvents
import com.android.aschat.feature_home.presentation.HomeViewModel
import com.android.aschat.feature_login.domain.model.coin.CoinGood
import com.android.aschat.util.DensityUtil
import com.android.aschat.util.equilibriumAssignmentOfLinear
import com.drake.brv.BindingAdapter
import com.drake.brv.listener.ItemDifferCallback
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CoinFragment: Fragment() {

    private lateinit var mBinding: HomeCoinFragmentBinding
    private val mViewModel: HomeViewModel by activityViewModels()

    // rv的adapter
    private lateinit var mRvAdapter: BindingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = HomeCoinFragmentBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        initWidget()
    }

    fun initBinding() {
        mBinding.apply {
            mRvAdapter = coinGoodsRv.linear().setup {
                addType<CoinGood>(R.layout.home_coin_goods_item)

                onClick(R.id.home_coin_goods_item){
                    mViewModel.onEvent(HomeEvents.ClickCoinGood(this.getModel()))
                }
                itemDifferCallback = object :ItemDifferCallback {
                    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                        val old = oldItem as CoinGood
                        val new = newItem as CoinGood
                        return old == new
                    }

                    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
                        val old = oldItem as CoinGood
                        val new = newItem as CoinGood
                        return old.goodsId == new.goodsId
                    }
                }
            }
            // 添加间距
            coinGoodsRv.addItemDecoration(object :RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    equilibriumAssignmentOfLinear(DensityUtil.dip2px(5f, requireContext()), outRect, view, parent)
                }
            })
            // 退出
            coinExit.setOnClickListener {
                mViewModel.onEvent(HomeEvents.ExitCoinGoods(findNavController()))
            }
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }
        mViewModel.apply {
            onEvent(HomeEvents.LoadCoin)
            coinGoods.observe(viewLifecycleOwner){
                mRvAdapter.setDifferModels(it, false)
            }
        }
    }

    fun initWidget() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        mViewModel.onEvent(HomeEvents.EndTimer)
    }
}
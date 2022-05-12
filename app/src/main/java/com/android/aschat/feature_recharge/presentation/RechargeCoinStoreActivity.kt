package com.android.aschat.feature_recharge.presentation

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.android.aschat.R
import com.android.aschat.databinding.RechargeActyBinding
import com.android.aschat.feature_host.domain.model.hostdetail.userinfo.HostInfo
import com.android.aschat.feature_login.domain.model.coin.CoinGood
import com.android.aschat.feature_rongyun.rongyun.model.RechargeInfo
import com.android.aschat.util.*
import com.drake.brv.BindingAdapter
import com.drake.brv.listener.ItemDifferCallback
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RechargeCoinStoreActivity : AppCompatActivity() {

    private val mViewModel: RechargeCoinStoreViewModel by viewModels()
    private lateinit var mBinding: RechargeActyBinding

    // rv的adapter
    private lateinit var mRvAdapter: BindingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置状态栏纯色
        StatusBarUtil.setColorNoTranslucent(this, resources.getColor(R.color.pink1))
        mBinding = DataBindingUtil.setContentView(this, R.layout.recharge_acty)

        val hostInfoStr = intent.getStringExtra("hostInfo")!!
        val goodsListStr = intent.getStringExtra("goodsList")!!
        val hostInfo = JsonUtil.json2Any(hostInfoStr, HostInfo::class.java)
        val goodsList: List<RechargeInfo> = JsonUtil.json2Any(goodsListStr, List::class.java, RechargeInfo::class.java)
        mViewModel.onEvent(RechargeEvents.InitRechargeViewModel(hostInfo, goodsList))
        initWidget()
    }

    fun initWidget() {
        mBinding.apply {
            rechargeCoinTitle.typeface = FontUtil.getTypeface(this@RechargeCoinStoreActivity)
            rechargeCoinHint1.typeface = FontUtil.getTypeface(this@RechargeCoinStoreActivity)
            rechargeCoinHint2.typeface = FontUtil.getTypeface(this@RechargeCoinStoreActivity)
            mRvAdapter = rechargeGoodsRv.linear().setup {
                addType<CoinGood>(R.layout.home_coin_goods_item)

                onClick(R.id.home_coin_goods_item){
                    // TODO: 点击充值

                }
                itemDifferCallback = object : ItemDifferCallback {
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
                onPayload {
                    setCoinGoodTagBack(this.findView(R.id.coin_goods_tag), this.getModel())
                }
            }
            // 添加间距
            rechargeGoodsRv.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    equilibriumAssignmentOfLinear(DensityUtil.dip2px(5f, this@RechargeCoinStoreActivity), outRect, view, parent)
                }
            })
            // 退出
            rechargeExit.setOnClickListener {
                mViewModel.onEvent(RechargeEvents.ExitActivity(this@RechargeCoinStoreActivity))
            }
        }
        mViewModel.apply {
            hostInfo.observe(this@RechargeCoinStoreActivity) {
                mBinding.rechargeHead.load(it.avatarUrl)
                mBinding.rechargeCoinHint1.text = getString(R.string.doyoulike) + it.nickname + " ?"
            }
            coinGoodsList.observe(this@RechargeCoinStoreActivity) {
                mRvAdapter.models = it
            }
        }
    }
}
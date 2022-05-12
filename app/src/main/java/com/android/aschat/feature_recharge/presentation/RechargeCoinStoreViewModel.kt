package com.android.aschat.feature_recharge.presentation

import android.content.Context
import androidx.lifecycle.*
import com.android.aschat.feature_host.domain.model.hostdetail.userinfo.HostInfo
import com.android.aschat.feature_host.domain.repo.HostRepo
import com.android.aschat.feature_login.domain.model.coin.CoinGood
import com.android.aschat.feature_rongyun.rongyun.model.RechargeInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class RechargeCoinStoreViewModel @Inject constructor(@Named("HostRepo") private val repo: HostRepo, @Named("Context") val context: Context) : ViewModel() {

    private val _hostInfo: MutableLiveData<HostInfo> = MutableLiveData()
    val hostInfo: LiveData<HostInfo> = _hostInfo

    private val _goodsList: MutableLiveData<List<RechargeInfo>> = MutableLiveData(emptyList())
    val coinGoodsList: LiveData<List<CoinGood>> = _goodsList.map { list ->
        val result = mutableListOf<CoinGood>()
        list.forEach {
            val coinGood = CoinGood(
                code = it.code,
                discount = it.discount,
                exchangeCoin = it.exchangeCoin,
                goodsId = it.goodsId,
                icon = it.icon,
                isPromotion = it.isPromotion,
                originalPrice = it.originalPrice,
                originalPriceRupee = 0.0,
                price = it.price,
                priceRupee = it.priceRupee,
                tags = it.tags,
                type = it.type,
                validity = 1,
                validityUnit = ""
            )
            result.add(coinGood)
        }
        result
    }


    fun onEvent(event: RechargeEvents) {
        when (event) {
            is RechargeEvents.InitRechargeViewModel -> {
                val hostInfo = event.hostInfo
                val goodsList = event.goodsList
                _hostInfo.postValue(hostInfo)
                _goodsList.postValue(goodsList)
            }
            is RechargeEvents.ExitActivity -> {
                event.activity.finish()
            }
        }
    }
}

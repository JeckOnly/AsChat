package com.android.aschat.feature_recharge.presentation

import com.android.aschat.feature_host.domain.model.hostdetail.userinfo.HostInfo
import com.android.aschat.feature_rongyun.rongyun.model.RechargeInfo

sealed class RechargeEvents {
    data class InitRechargeViewModel(val hostInfo: HostInfo, val goodsList: List<RechargeInfo>): RechargeEvents()
    class ExitActivity(val activity: RechargeCoinStoreActivity): RechargeEvents()
}
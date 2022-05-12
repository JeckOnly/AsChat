package com.android.aschat.feature_rongyun.rongyun.model

data class RechargeInfo(
    val code: String = "",
    val discount: Double = 0.0,
    val exchangeCoin: Int = 0,
    val extraCoin: Int = 0,
    val extraCoinPercent: Int = 0,
    val goodsId: String = "",
    val icon: String = "",
    val invitationId: String = "",
    val isPromotion: Boolean = false,
    val originalPrice: Double = 0.0,
    val originalPriceRupee: Double = 0.0,
    val price: Double = 0.0,
    val priceRupee: Double = 0.0,
    val rechargeNum: Int = 0,
    val remainMilliseconds: String = "",
    val type: String = "",
    val tags: String = ""
)
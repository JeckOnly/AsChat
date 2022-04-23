package com.android.aschat.feature_login.domain.model.coin

data class CoinGoodPromotion(
    val code: String,
    val discount: Double,
    val exchangeCoin: Int,
    val goodsId: String,
    val icon: String,
    val isPromotion: Boolean,
    val originalPrice: Double,
    val originalPriceRupee: Double,
    val price: Double,
    val priceRupee: Double,
    val surplusMillisecond: String,
    val tags: String,
    val type: String
)
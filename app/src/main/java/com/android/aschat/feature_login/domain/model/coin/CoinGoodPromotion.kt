package com.android.aschat.feature_login.domain.model.coin

/**
 * 补充一些默认的非法字段
 */
data class CoinGoodPromotion(
    val code: String = "",
    val discount: Double = 0.0,
    val exchangeCoin: Int = 0,
    val goodsId: String = "",
    val icon: String = "",
    val isPromotion: Boolean = false,
    val originalPrice: Double = 0.0,
    val originalPriceRupee: Double = 0.0,
    val price: Double = 0.0,
    val priceRupee: Double = 0.0,
    val surplusMillisecond: String = "",
    val tags: String = "",
    val type: String = ""
) {
    fun toCoinGood(): CoinGood {
        return CoinGood(
            code = code,
            discount = discount,
            exchangeCoin = exchangeCoin,
            goodsId = goodsId,
            icon = icon,
            isPromotion = isPromotion,
            originalPrice = originalPrice,
            originalPriceRupee = originalPriceRupee,
            price = price,
            priceRupee = priceRupee,
            tags = tags,
            type = type,
            validity = 0,
            validityUnit = "",
            surplusMillisecond = surplusMillisecond.toLong()
        )
    }
}
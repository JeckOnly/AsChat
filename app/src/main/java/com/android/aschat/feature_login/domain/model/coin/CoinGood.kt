package com.android.aschat.feature_login.domain.model.coin

data class CoinGood(
    val code: String,// 商品编号
    val discount: Double,// 折扣
    val exchangeCoin: Int,// 兑换金币数
    val goodsId: String,// 商品id
    val icon: String,// 商品图标
    val isPromotion: Boolean,// 是否促销
    val originalPrice: Double,// 原价
    val originalPriceRupee: Double,// 原价(卢比)
    val price: Double,// 当前价格
    val priceRupee: Double,// 当前价格(卢比)
    val tags: String,// 商品标签
    val type: String,// 商品类型
    val validity: Int,// 订阅有效期
    val validityUnit: String// 订阅有效期单位
)
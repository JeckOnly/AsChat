package com.android.aschat.feature_login.domain.model.coin

data class GetCoinGood(
    val isIncludeSubscription: Boolean,
    val payChannel: String
)
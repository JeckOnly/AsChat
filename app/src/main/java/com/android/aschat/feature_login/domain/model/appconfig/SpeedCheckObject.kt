package com.android.aschat.feature_login.domain.model.appconfig

data class SpeedCheckObject(
    val interval: Int,
    val sizeLimit: Int,
    val url: String
)
package com.android.aschat.feature_login.domain.model.appconfig

data class UpgradeObject(
    val always_show: String,
    val force: String,
    val pkgname: String,
    val upgradeContent: String,
    val ver: String
)
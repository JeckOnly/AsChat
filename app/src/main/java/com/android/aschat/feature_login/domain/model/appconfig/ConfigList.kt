package com.android.aschat.feature_login.domain.model.appconfig

data class ConfigList(
    var items: List<ConfigItemBase>,
    val ver: String
)
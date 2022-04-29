package com.android.aschat.feature_home.domain.model.mine

data class SaveUserInfo(
    val about: String,
    val birthday: String,
    val country: String,
//    val language: String,这个字段暂时不会更改
    val nickname: String
)
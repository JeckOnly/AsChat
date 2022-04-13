package com.android.aschat.feature_login.domain.model.login

data class LoginData(
    val isFirstRegister: Boolean,
    val token: String,
    val userInfo: UserInfo
)
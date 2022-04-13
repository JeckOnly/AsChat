package com.android.aschat.feature_login.domain.model.strategy

data class FlashChatConfig(
    val isFreeCall: Boolean,
    val isSwitch: Boolean,
    val residueFreeCallTimes: Int
)
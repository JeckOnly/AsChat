package com.android.aschat.feature_rongyun.rongyun.model

import com.android.aschat.common.Constants

data class GetRechargeInfo(
    val invitationId: String,
    val payChannel: String = Constants.PayChannel
)
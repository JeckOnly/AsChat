package com.android.aschat.feature_home.domain.model.blocked

data class BlockedItem(
    val age: Int,
    val avatar: String,
    val broadcasterId: String,
    val gender: Int,
    val nickName: String,
    val registerCountry: String
)
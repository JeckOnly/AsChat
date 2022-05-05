package com.android.aschat.common.network.socketIo.model

data class OnPickUpModel(
    val channelName: String,
    val chooseVideoSdk: String,
    val clientSessionId: String,
    val fromUserId: String,
    val isFree: String,
    val rtcToken: String,
    val toUserId: String,
    val videoFileUrl: String,
    val videoPlayMode: String
)
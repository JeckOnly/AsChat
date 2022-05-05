package com.android.aschat.common.network.socketIo.model

data class OnHangUpModel(
    val channelName: String,
    val fromUserId: String,
    val reason: Int,
    val toUserId: String
)

//{
//    "channelName": "通话频道",
//    "fromUserId": "拨打方用户id",
//    "toUserId": "被拨打方用户id",
//    "reason": 1,  // 退出视频原因
//}
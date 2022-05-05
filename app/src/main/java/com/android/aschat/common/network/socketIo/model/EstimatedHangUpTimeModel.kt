package com.android.aschat.common.network.socketIo.model

data class EstimatedHangUpTimeModel(
    val channelName: String,
    val estimateTime: Long,
    val payUserId: String
)

//{
//    "channelName":"",
//    "payUserId":"付费用户id",
//    "estimateTime": 888888888888888 // 预估剩余时间，单位：秒
//}
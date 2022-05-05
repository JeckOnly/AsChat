package com.android.aschat.common.network.socketIo.model

data class MessageEventModel(
    val command: String,
    val content: String,
    val fromUserId: String,
    val timestamp: Long,
    val toUserId: String
)

//{
//    "fromUserId": "",
//    "toUserId": "",
//    "content": "对方发来的消息文本内容",
//    "timestamp": long -> 消息构建时间,
//    "command": ""
//}
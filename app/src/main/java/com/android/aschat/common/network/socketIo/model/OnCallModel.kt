package com.android.aschat.common.network.socketIo.model

data class OnCallModel(
    val age: Int,
    val avatar: String,
    val avatarThumbUrl: String,
    val broadcasterUnitPrice: Int,
    val channelName: String,
    val chooseVideoSdk: Int,
    val country: String,
    val fromUserId: String,
    val gender: Int,
    val isFree: Boolean,
    val isFriend: Boolean,
    val nickname: String,
    val rtcToken: String,
    val toUserId: String,
    val uiTips: String,
    val videoFileUrl: String,
    val videoPlayMode: String
)

//{
//    "channelName": "通话频道",
//    "avatar": "拨打方头像",
//    "avatarThumbUrl": "拨打方头像缩略图",
//    "fromUserId": "拨打方用户id",
//    "toUserId": "被拨打方用户id",
//    "videoPlayMode": "视频播放模式",
//    "videoFileUrl": "视频文件Url",
//    "age": 0, //拨打方年龄
//    "country": "", // 拨打方国家
//    "isFree": false, // 是否免费通话
//    "isFriend": false, // 是否好友
//    "chooseVideoSdk": 1, // 选用的视频sdk: 1- > 声网；2 -> bigo；
//    "nickname": "拨打方昵称",
//    "gender": 1, // 拨打方性别
//    "broadcasterUnitPrice": -1, // 主播通话单价
//    "rtcToken": "声网token",
//    "uiTips": "拨打与被拨打界面显示的分钟话费文案"
//}

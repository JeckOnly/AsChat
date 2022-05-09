package com.android.aschat.common

import com.android.aschat.R

object Constants {
    const val Default_Birthday = "1995-1-1"
    const val OauthType = 4
    const val Max_Retry = Int.MAX_VALUE

    /**
     * 每次请求主播的条数
     */
    const val HostWall_LimitPlus = 20

    /**
     * 每次请求关注列表的条数
     */
    const val Follow_LimitPlus = 20

    const val Media_Type_Video = "video"
    const val Media_Type_Photo = "photo"

    /**
     * 消息页, 文本为id
     */
    val MCF_List = listOf<Int>(R.string.messages, R.string.calls, R.string.Followed)

    /**
     * 主播页，文本为id
     */
    val RANK_List = listOf<Int>(R.string.charm, R.string.rich)

    // 主播状态
    const val Online = "Online"
    const val Busy = "Busy"
    const val Incall = "Incall"
    const val Offline = "Offline"
    const val Avaliable = "Available"

    // 前往主播资料页的来源
    const val WhereFrom = "WhereFrom"
    const val FromWall = 0
    const val FromFollow = 1
    const val FromBlock = 2
    const val FromRank = 3
    const val FromRongyunList = 4

    const val PayChannel = "GP"

    // 屏蔽举报模块
    const val Block = "Block"
    const val Report = "Report"

    // 举报要传递的业务分类子项，屏蔽传“”就行
    const val Porngraphic = "Porngraphic"
    const val Falsegender = "False gender"
    const val Fraud = "Fraud"
    const val Politiclsensitive = "Politicl sensitive"
    const val Other = "Other"

    // appconfig key相关
    const val MicroTransName = "microsoft_translation_key"
    const val RckName = "rck"
    const val RtckName = "rtck"

    // okhttp error json
    const val Error_Custom_Json = "{\"code\":123456,\"key\":\"okhttp custom error\",\"msg\":\"okhttp custom error\"}"

    // 排行榜请求的数量
    const val Rank_Count = 50

    // 列表刷新时间戳，10s
    const val Host_Refresh_Stamp = 10 * 1000L
}
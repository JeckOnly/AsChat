package com.android.aschat.common

import com.android.aschat.R

object Constants {
    const val Default_Birthday = "1995-1-1"
    const val OauthType = 4
    const val Max_Retry = 10

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

    const val PayChannel = "GP"

    // 时间相关
    const val HOUR_MILLIS = 1000L * 60 * 60
    const val MIN_MILLIS = 1000L * 60
    const val SECOND_MILLIS = 1000L

    // 屏蔽举报模块
    const val Block = "Block"
    const val Report = "Report"

    // 举报要传递的业务分类子项，屏蔽传“”就行
    const val Porngraphic = "Porngraphic"
    const val Falsegender = "False gender"
    const val Fraud = "Fraud"
    const val Politiclsensitive = "Politicl sensitive"
    const val Other = "Other"
}
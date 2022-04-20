package com.android.aschat.common

import com.android.aschat.R

object Constants {
    const val Default_Birthday = "1995-1-1"
    const val OauthType = 4
    const val Login_Success_Key = "common_success"
    const val Max_Retry = 10

    /**
     * 每次请求主播的条数
     */
    const val HostWall_LimitPlus = 20

    const val Media_Type_Video = "video"
    const val Media_Type_Photo = "photo"

    /**
     * 消息页, 文本为id
     */
    val MCF_List = listOf<Int>(R.string.messages, R.string.calls, R.string.Followed)
}
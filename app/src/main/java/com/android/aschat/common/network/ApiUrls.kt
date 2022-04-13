package com.android.aschat.common.network

object ApiUrls {
    /**
     * base url 【http://12345.1234:1234】
     */
    const val BASE_URL: String = "http://52.11.57.160:2030"

    /**
     * 登录
     */
    const val Login: String = "$BASE_URL/security/oauth"

    /**
     * 策略信息
     */
    const val GetStrategy = "$BASE_URL/config/getStrategy"

    /**
     * 搜索主播墙 主播列表，需要传一个json
     */
    const val GetHostList = "$BASE_URL/broadcaster/wall/search"
}
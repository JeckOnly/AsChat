package com.android.aschat.common.network

import java.util.*

object ApiUrls {
    /**
     * base url 【http://12345.1234:1234】
     */
    const val BASE_URL: String = "http://52.11.57.160:2030"

    /**
     * base 长连url
     */
    const val BASE_SOCKET_URL: String = "http://52.11.57.160:9001"

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

    /**
     * 添加关注
     */
    const val AddFriend = "$BASE_URL/user/addFriend"

    /**
     * 取消关注
     */
    const val CancelFriend = "$BASE_URL/user/unfriend"

    /**
     * 获得单个主播信息
     */
    const val GetUserInfo = "$BASE_URL/user/getUserInfo"

    /**
     * 获得主播额外信息：礼物，标签
     */
    const val GetExtraInfo = "$BASE_URL/user/getBroadcasterExtraInfo"

    /**
     * 获得关注的用户列表
     */
    const val GetFriends = "$BASE_URL/user/getFriendsListPage"

    /**
     * 获取礼物策略
     */
    const val GetGiftInfo = "$BASE_URL/gift/list"

    /**
     * oss信息获取
     */
    const val GetOssInfo = "$BASE_URL/user/oss/policy"

    /**
     * 获取金币商品链接
     */
    const val GetCoinGoods = "$BASE_URL/coin/goods/search"

    /**
     * 获取金币商品链接
     */
    const val GetCoinGoodsPromotion = "$BASE_URL/coin/goods/getPromotion"

}
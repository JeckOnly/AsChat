package com.android.aschat.feature_host.domain.repo

import com.android.aschat.common.network.AppServices
import com.android.aschat.common.network.Response
import com.android.aschat.feature_host.domain.model.hostdetail.extrainfo.GiftAndLabel
import com.android.aschat.feature_host.domain.model.hostdetail.extrainfo.GiftInfo
import com.android.aschat.feature_host.domain.model.hostdetail.friend.AddFriend
import com.android.aschat.feature_host.domain.model.hostdetail.friend.CancelFriend
import com.android.aschat.feature_host.domain.model.hostdetail.userinfo.HostInfo
import com.android.aschat.util.LogUtil

class HostRepo(private val services: AppServices) {

    /**
     * 关注主播
     */
    suspend fun addFriend(addFriend: AddFriend): Response<Boolean> {
        var response: Response<Boolean> = services.addFriend(addFriend)
        while (response.code != 0) {
            LogUtil.d("关注主播 ${response.code}")
            response = services.addFriend(addFriend)
        }
        return response
    }

    /**
     * 取消关注主播
     */
    suspend fun cancelFriend(cancelFriend: CancelFriend): Response<Boolean> {
        var response: Response<Boolean> = services.cancelFriend(cancelFriend)
        while (response.code != 0) {
            LogUtil.d("取消关注主播 ${response.code}")
            response = services.cancelFriend(cancelFriend)
        }
        return response
    }

    /**
     * 获得单个user信息
     */
    suspend fun getHostInfo(userId: String): Response<HostInfo> {
        var response: Response<HostInfo> = services.getUserInfo(userId)
        return response
    }

    /**
     * 获取主播额外信息
     */
    suspend fun getExtraInfo(userId: String): Response<GiftAndLabel> {
        var response: Response<GiftAndLabel> = services.getExtraInfo(userId)
        return response
    }

    /**
     * 获取礼物策略
     */
    suspend fun getGiftInfo(): Response<List<GiftInfo>> {
        return services.getGiftInfo()
    }
}
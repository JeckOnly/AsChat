package com.android.aschat.feature_home.domain.repo

import com.android.aschat.common.network.AppServices
import com.android.aschat.common.network.Response
import com.android.aschat.feature_home.domain.model.wall.subtag.GetHostInfo
import com.android.aschat.feature_home.domain.model.wall.subtag.HostData
import com.android.aschat.feature_host.domain.model.hostdetail.extrainfo.GiftAndLabel
import com.android.aschat.feature_host.domain.model.hostdetail.friend.AddFriend
import com.android.aschat.feature_host.domain.model.hostdetail.friend.CancelFriend
import com.android.aschat.feature_host.domain.model.hostdetail.userinfo.HostInfo
import com.android.aschat.util.LogUtil

class HomeRepo(private val services: AppServices) {

    /**
     * 得到主播数据
     */
    suspend fun getHostData(getHostInfo: GetHostInfo): Response<List<HostData>> {
        var response: Response<List<HostData>> = services.getHostList(getHostInfo)
        while (response.code != 0) {
            LogUtil.d("获取主播${getHostInfo.category}  ${getHostInfo.tag}: ${response.code}")
            response = services.getHostList(getHostInfo)
        }
        return response
    }
}
package com.android.aschat.feature_rongyun.rongyun.ui

import android.net.Uri
import com.android.aschat.common.network.Response
import com.android.aschat.feature_host.domain.model.hostdetail.userinfo.HostInfo
import io.rong.imkit.RongIM
import io.rong.imkit.userinfo.UserDataProvider
import io.rong.imlib.model.UserInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * 融云的用户信息提供者
 */
class MyUserInfoProvider(val getUserInfoMethod: suspend (String) -> Response<HostInfo>): UserDataProvider.UserInfoProvider {
    override fun getUserInfo(userId: String): UserInfo? {
        CoroutineScope(Dispatchers.Main).launch {
            val response = getUserInfoMethod(userId)
            if (response.code == 0) {
                val hostInfo = response.data
                val userInfo = UserInfo(
                    userId,
                    hostInfo!!.nickname,
                    Uri.parse(hostInfo.avatarUrl)
                )
                RongIM.getInstance().refreshUserInfoCache(userInfo)
            }else {
                val userInfo = UserInfo(
                    "",
                    "",
                    Uri.parse("")
                )
                RongIM.getInstance().refreshUserInfoCache(userInfo)
            }
        }
        return null
    }
}
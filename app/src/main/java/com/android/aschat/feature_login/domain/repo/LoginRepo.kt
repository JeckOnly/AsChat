package com.android.aschat.feature_login.domain.repo

import com.android.aschat.common.network.AppServices
import com.android.aschat.common.network.Response
import com.android.aschat.feature_login.data.UserDao
import com.android.aschat.feature_login.domain.model.login.LoginData
import com.android.aschat.feature_login.domain.model.login.UserInfo
import com.android.aschat.feature_login.domain.model.strategy.StrategyData
import com.android.aschat.util.LogUtil

class LoginRepo(private val services: AppServices, private val dao: UserDao) {
    suspend fun login(oauthType: Int,token: String): Response<LoginData> {
        var response: Response<LoginData> = services.login(oauthType, token)
        while (response.code != 0) {
            LogUtil.d("登录: ${response.code}")
            response = services.login(oauthType, token)
        }
        return response
    }

    /**
     * 获取策略
     *
     * 失败会不断重试
     */
    suspend fun getStrategy(): Response<StrategyData>{
        var response: Response<StrategyData> = services.getStrategy()
        while (response.code != 0) {
            LogUtil.d("strategy: ${response.code}")
            response = services.getStrategy()
        }
        return response
    }

    @Deprecated("改用sp")
    suspend fun queryByUserId(userId: String): UserInfo{
        return dao.queryByUserId(userId)
    }

    @Deprecated("改用sp")
    suspend fun insertUserInfo(userInfo: UserInfo) {
        dao.insertUserInfo(userInfo)
    }

    @Deprecated("改用sp")
    suspend fun updateUserInfo(userInfo: UserInfo):Int {
        return dao.updateUserInfo(userInfo)
    }
}
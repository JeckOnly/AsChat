package com.android.aschat.feature_login.domain.repo

import com.android.aschat.common.network.AppServices
import com.android.aschat.common.network.Response
import com.android.aschat.feature_login.data.UserDao
import com.android.aschat.feature_login.domain.model.login.LoginData
import com.android.aschat.feature_login.domain.model.login.UserInfo
import com.android.aschat.feature_login.domain.model.strategy.StrategyData

class LoginRepo(private val services: AppServices, private val dao: UserDao) {
    suspend fun login(oauthType: Int,token: String): Response<LoginData> {
        return services.login(oauthType, token)
    }

    suspend fun getStrategy(): Response<StrategyData>{
        return services.getStrategy()
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
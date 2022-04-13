package com.android.aschat.common.network

import com.android.aschat.feature_login.domain.model.login.LoginData
import com.android.aschat.feature_login.domain.model.strategy.StrategyData
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface AppServices {

    /**
     * oauthType：Int （游客登录：4）
     * token :String （Android设备号）
     */
   @FormUrlEncoded
   @POST(ApiUrls.Login)
   suspend fun login(@Field("oauthType") oauthType: Int, @Field("token") token: String): Response<LoginData>

   @GET(ApiUrls.GetStrategy)
   suspend fun getStrategy(): Response<StrategyData>
   
//   @POST(ApiUrls.GetHostList)
//   suspend fun getHostList(): Response<// TODO: 主播实体 >
}
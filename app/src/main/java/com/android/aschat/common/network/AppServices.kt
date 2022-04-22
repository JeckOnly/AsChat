package com.android.aschat.common.network

import com.android.aschat.feature_home.domain.model.follow.FollowFriend
import com.android.aschat.feature_home.domain.model.follow.GetFriendList
import com.android.aschat.feature_home.domain.model.wall.subtag.GetHostInfo
import com.android.aschat.feature_home.domain.model.wall.subtag.HostData
import com.android.aschat.feature_host.domain.model.hostdetail.extrainfo.GiftAndLabel
import com.android.aschat.feature_host.domain.model.hostdetail.extrainfo.GiftInfo
import com.android.aschat.feature_host.domain.model.hostdetail.friend.AddFriend
import com.android.aschat.feature_host.domain.model.hostdetail.friend.CancelFriend
import com.android.aschat.feature_host.domain.model.hostdetail.userinfo.HostInfo
import com.android.aschat.feature_login.domain.model.login.LoginData
import com.android.aschat.feature_login.domain.model.strategy.StrategyData
import retrofit2.http.*

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
   
   @POST(ApiUrls.GetHostList)
   suspend fun getHostList(@Body getHostInfo: GetHostInfo): Response<List<HostData>>

   @POST(ApiUrls.AddFriend)
   suspend fun addFriend(@Body addFriend: AddFriend): Response<Boolean>


   @POST(ApiUrls.CancelFriend)
   suspend fun cancelFriend(@Body cancelFriend: CancelFriend): Response<Boolean>

   @GET(ApiUrls.GetUserInfo)
   suspend fun getUserInfo(@Query("userId") userId: String): Response<HostInfo>

   @GET(ApiUrls.GetExtraInfo)
   suspend fun getExtraInfo(@Query("userId") userId: String): Response<GiftAndLabel>

   @POST(ApiUrls.GetFriends)
   suspend fun getFriendList(@Body getFriendList: GetFriendList): Response<List<FollowFriend>>

   @GET(ApiUrls.GetGiftInfo)
   suspend fun getGiftInfo(): Response<List<GiftInfo>>
}
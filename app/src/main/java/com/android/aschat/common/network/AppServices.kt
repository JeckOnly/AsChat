package com.android.aschat.common.network

import com.android.aschat.feature_home.domain.model.blocked.BlockOrReport
import com.android.aschat.feature_home.domain.model.blocked.BlockedItem
import com.android.aschat.feature_home.domain.model.blocked.CancelBlock
import com.android.aschat.feature_home.domain.model.follow.FollowFriend
import com.android.aschat.feature_home.domain.model.follow.GetFriendList
import com.android.aschat.feature_home.domain.model.mine.SaveUserInfo
import com.android.aschat.feature_home.domain.model.mine.UpdateAvatar
import com.android.aschat.feature_home.domain.model.mine.UpdateAvatarResult
import com.android.aschat.feature_home.domain.model.wall.subtag.GetHostInfo
import com.android.aschat.feature_home.domain.model.wall.subtag.HostData
import com.android.aschat.feature_host.domain.model.hostdetail.extrainfo.GiftAndLabel
import com.android.aschat.feature_host.domain.model.hostdetail.extrainfo.GiftInfo
import com.android.aschat.feature_host.domain.model.hostdetail.friend.AddFriend
import com.android.aschat.feature_host.domain.model.hostdetail.friend.CancelFriend
import com.android.aschat.feature_host.domain.model.hostdetail.userinfo.HostInfo
import com.android.aschat.feature_login.domain.model.appconfig.ConfigList
import com.android.aschat.feature_login.domain.model.coin.CoinGood
import com.android.aschat.feature_login.domain.model.coin.CoinGoodPromotion
import com.android.aschat.feature_login.domain.model.coin.GetCoinGood
import com.android.aschat.feature_login.domain.model.login.LoginData
import com.android.aschat.feature_login.domain.model.osspolicy.OssPolicy
import com.android.aschat.feature_login.domain.model.osspolicy.OssResult
import com.android.aschat.feature_login.domain.model.strategy.StrategyData
import com.google.android.exoplayer2.text.span.TextAnnotation
import okhttp3.MultipartBody
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

   @POST(ApiUrls.GetCoinGoods)
   suspend fun getCoinGoods(@Body getCoinGood: GetCoinGood): Response<List<CoinGood>>

   @POST(ApiUrls.GetCoinGoodsPromotion)
   suspend fun getCoinGoodsPromotion(@Body getCoinGood: GetCoinGood): Response<CoinGoodPromotion>

   @POST(ApiUrls.GetBlockedList)
   suspend fun getBlockedList(): Response<List<BlockedItem>>

   @POST(ApiUrls.BlockOrReport)
   suspend fun blockOrReport(@Body blockOrReport: BlockOrReport): Response<Boolean>

   @POST(ApiUrls.CancelBlock)
   suspend fun cancelBlock(@Body cancelBlock: CancelBlock): Response<Boolean>

   @GET(ApiUrls.GetUserStatus)
   suspend fun getUserStatus(@Query("userId") userId: String): Response<String>

   @GET(ApiUrls.GetAppConfig)
   suspend fun getAppConfig(@Query("ver") ver: Int): Response<ConfigList>

   @GET(ApiUrls.GetOssInfo)
   suspend fun getOssPolicy(): Response<OssPolicy>

   @Multipart
   @POST
   suspend fun uploadFile(@Url host: String, @Part parts: List<MultipartBody.Part>): Response<OssResult>

   @POST(ApiUrls.UpdateAvatar)
   suspend fun updateAvatar(@Body updateAvatar: UpdateAvatar): Response<UpdateAvatarResult>

   @POST(ApiUrls.SaveUserBasicInfo)
   suspend fun saveUserBasicInfo(@Body saveUserInfo: SaveUserInfo): Response<Boolean>
}
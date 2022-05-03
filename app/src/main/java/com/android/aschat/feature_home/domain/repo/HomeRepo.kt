package com.android.aschat.feature_home.domain.repo

import com.android.aschat.common.network.AppServices
import com.android.aschat.common.network.Response
import com.android.aschat.feature_home.domain.model.blocked.BlockedItem
import com.android.aschat.feature_home.domain.model.follow.FollowFriend
import com.android.aschat.feature_home.domain.model.follow.GetFriendList
import com.android.aschat.feature_home.domain.model.mine.SaveUserInfo
import com.android.aschat.feature_home.domain.model.mine.UpdateAvatar
import com.android.aschat.feature_home.domain.model.mine.UpdateAvatarResult
import com.android.aschat.feature_home.domain.model.wall.subtag.GetHostInfo
import com.android.aschat.feature_home.domain.model.wall.subtag.HostData
import com.android.aschat.feature_host.domain.model.hostdetail.extrainfo.GiftAndLabel
import com.android.aschat.feature_host.domain.model.hostdetail.friend.AddFriend
import com.android.aschat.feature_host.domain.model.hostdetail.friend.CancelFriend
import com.android.aschat.feature_host.domain.model.hostdetail.userinfo.HostInfo
import com.android.aschat.feature_login.domain.model.osspolicy.OssResult
import com.android.aschat.util.LogUtil
import okhttp3.MultipartBody
import retrofit2.http.Body

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

    /**
     * 得到关注列表
     */
    suspend fun getFriendList(getFriendList: GetFriendList): Response<List<FollowFriend>> {
        var response: Response<List<FollowFriend>> = services.getFriendList(getFriendList)
        while (response.code != 0) {
            LogUtil.d("获取关注列表${getFriendList.limit}")
            response = services.getFriendList(getFriendList)
        }
        return response
    }

    /**
     * 获取屏蔽列表
     */
    suspend fun getBlockedList(): Response<List<BlockedItem>>{
        val response = services.getBlockedList()
        return response
    }

    /**
     * 上传照片到oss
     */
    suspend fun uploadFile(host: String, part: List<MultipartBody.Part>): Response<OssResult> {
        val response = services.uploadFile(host, part)
        return response
    }

    /**
     * 把上传照片到oss的结果上传到自己服务器
     */
    suspend fun updateAvatar(updateAvatar: UpdateAvatar): Response<UpdateAvatarResult> {
        val response = services.updateAvatar(updateAvatar)
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
     * 保存用户基本信息
     */
    suspend fun saveUserBasicInfo(saveUserInfo: SaveUserInfo): Response<Boolean> {
        var response: Response<Boolean> = services.saveUserBasicInfo(saveUserInfo)
        return response
    }

    /**
     * 获取一批用户的状态
     */
    suspend fun getUserStatusList(userIdList: List<String>): Response<Map<String, String>> {
        val response: Response<Map<String, String>> = services.getUserStatusList(userIdList)
        return response
    }
}
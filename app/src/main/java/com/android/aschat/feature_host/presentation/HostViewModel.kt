package com.android.aschat.feature_host.presentation

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import com.android.aschat.R
import com.android.aschat.common.Constants
import com.android.aschat.feature_home.domain.model.wall.subtag.HostData
import com.android.aschat.feature_home.domain.repo.HomeRepo
import com.android.aschat.feature_host.domain.model.hostdetail.friend.AddFriend
import com.android.aschat.feature_host.domain.model.hostdetail.friend.CancelFriend
import com.android.aschat.feature_host.domain.model.hostdetail.userinfo.HostInfo
import com.android.aschat.feature_host.domain.repo.HostRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.internal.notifyAll
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class HostViewModel @Inject constructor(@Named("HostRepo") private val repo: HostRepo, @Named("Context") val context: Context) : ViewModel() {

    // 主播详细资料界面

    // 当前查看的主播
    private val _hostData: MutableLiveData<HostData> = MutableLiveData()
    val hostData: LiveData<HostData> = _hostData

    private val _hostInfo: MutableLiveData<HostInfo> = MutableLiveData()
    val hostInfo: LiveData<HostInfo> = _hostInfo

    /**
     * 视频缩略图
     */
    val videoThumbImages: LiveData<List<String>> = hostInfo.map { hostInfo ->
        val mediaList = hostInfo.mediaList
        val newList = mediaList.filter {
            it.mediaType == Constants.Media_Type_Video
        }
        val urlList = newList.map {
            it.thumbUrl
        }
        return@map urlList
    }

    val videoUrls: LiveData<List<String>> = hostInfo.map { hostInfo ->
        val mediaList = hostInfo.mediaList
        val newList = mediaList.filter {
            it.mediaType == Constants.Media_Type_Video
        }
        val urlList = newList.map {
            it.mediaUrl
        }
        return@map urlList
    }

    val pictureUrls: LiveData<List<String>> = hostInfo.map { hostInfo ->
        val mediaList = hostInfo.mediaList
        val newList = mediaList.filter {
            it.mediaType == Constants.Media_Type_Photo
        }
        val urlList = newList.map {
            it.mediaUrl
        }
        return@map urlList
    }

    /**
     * 标签名称
     */
    private val _labelList: MutableLiveData<List<String>> = MutableLiveData()
    val labelList: LiveData<List<String>> = _labelList

    /**
     * 当前点击的图片position
     */
    var picturePositionClicked = -1

    /**
     * 当前点击的视频position
     */
    var videoPositionClicked = -1

    /**
     * 是否在线，后面不知道要做不做定时刷新
     */
    private val _status: MutableLiveData<String> = MutableLiveData()
    val status: LiveData<String> = _status

    /**
     * 是否follow
     */
    private val _follow: MutableLiveData<Boolean> = MutableLiveData(false)
    val follow: LiveData<Boolean> = _follow

    init {
        _hostData.observeForever { hostData ->
            viewModelScope.launch {
                launch {
                    val response = repo.getHostInfo(hostData.userId)
                    if (response.code == 0) {
                        // 成功
                        val hostInfo = response.data
                        _hostInfo.postValue(hostInfo)
                        _follow.postValue(hostInfo.isFriend)
                    }else {
                        // 失败
                    }
                }
                launch {
                    val response = repo.getExtraInfo(hostData.userId)
                    if (response.code == 0) {
                        // 成功
                        _labelList.postValue(response.data.labelsList)
                    }else {
                        // 失败
                    }
                }
            }
            // 更新_status
            _status.postValue(hostData.status)
        }
    }

    fun onEvent(event: HostEvents) {
        when (event) {
            is HostEvents.SendHostData -> {
                // 更改数据
                _hostData.postValue(event.hostData)
            }
            is HostEvents.SendFriendData -> {
                // 更改数据
                val friend = event.friendData
                // 从关注列表过来，也只好凑一个host data了
                val hostData = HostData(
                    age = friend.age,
                    applicableTags = emptyList(),
                    avatar = friend.avatarUrl,
                    avatarMapPath = friend.avatar,
                    callCoins = 0,
                    country = friend.country,
                    followNum = 0,
                    gender = friend.gender,
                    isFriend = true,
                    isMultiple = false,
                    nickname = friend.nickname,
                    status = friend.onlineStatus,
                    unit = "min",
                    userId = friend.userId,
                    videoMapPaths = emptyList()
                )
                _hostData.postValue(hostData)
            }
            is HostEvents.ExitHostDetail -> {
                // 退出该activity
                event.activity.finish()
            }
            is HostEvents.JumpFullScreenImage -> {
                // 跳转全屏查看图片
                picturePositionClicked = event.position
                event.navController.navigate(R.id.action_hostDetailFragment_to_fullScreenImageFragment)
            }
            is HostEvents.JumpFullScreenVideo -> {
                // 跳转全屏视频
                videoPositionClicked = event.position
                event.navController.navigate(R.id.action_hostDetailFragment_to_showShortVideoFragment)
            }
            is HostEvents.ExitFullScreenImage -> {
                // 退出全屏查看图片
                event.navController.popBackStack()
            }
            is HostEvents.ExitFullScreenVideo -> {
                // 退出全屏查看视频
                event.navController.popBackStack()
            }
            is HostEvents.ClickFollow -> {
                // 关注或取关主播
                 viewModelScope.launch {
                     if (_follow.value!!) {
                         // 原来关注，现在取关
                         val response = repo.cancelFriend(CancelFriend(_hostInfo.value?.userId?:""))
                         if (response.code == 0 && response.data) {
                             _follow.postValue(false)
                             Toast.makeText(context, context.getString(R.string.unfollow_success), Toast.LENGTH_SHORT).show()
                         }
                     }else {
                         // 原来没有关注，现在关注
                         val response = repo.addFriend(AddFriend(_hostInfo.value?.userId?:""))
                         if (response.code == 0 && response.data) {
                             _follow.postValue(true)
                             Toast.makeText(context, context.getString(R.string.follow_success), Toast.LENGTH_SHORT).show()
                         }
                     }
                 }
            }
        }
    }
}

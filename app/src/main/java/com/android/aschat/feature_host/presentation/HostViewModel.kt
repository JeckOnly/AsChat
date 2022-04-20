package com.android.aschat.feature_host.presentation

import android.content.Context
import androidx.lifecycle.*
import com.android.aschat.R
import com.android.aschat.common.Constants
import com.android.aschat.feature_home.domain.model.wall.subtag.HostData
import com.android.aschat.feature_home.domain.repo.HomeRepo
import com.android.aschat.feature_host.domain.model.hostdetail.userinfo.HostInfo
import com.android.aschat.feature_host.domain.repo.HostRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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

    init {
        _hostData.observeForever { hostData ->
            viewModelScope.launch {
                launch {
                    val response = repo.getHostInfo(hostData.userId)
                    if (response.code == 0) {
                        // 成功
                        _hostInfo.postValue(response.data)
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
        }
    }

    fun onEvent(event: HostEvents) {
        when (event) {
            is HostEvents.SendHostData -> {
                // 更改数据
                _hostData.postValue(event.hostData)
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
        }
    }
}

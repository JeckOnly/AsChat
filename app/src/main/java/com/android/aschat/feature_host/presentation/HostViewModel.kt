package com.android.aschat.feature_host.presentation

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import com.android.aschat.R
import com.android.aschat.common.Constants
import com.android.aschat.common.Gift
import com.android.aschat.common.giftAndNum2Gift
import com.android.aschat.common.giftStr2Gift
import com.android.aschat.feature_home.domain.model.blocked.BlockOrReport
import com.android.aschat.feature_home.domain.model.blocked.CancelBlock
import com.android.aschat.feature_home.domain.model.wall.subtag.HostData
import com.android.aschat.feature_host.domain.model.hostdetail.extrainfo.GiftInfo
import com.android.aschat.feature_host.domain.model.hostdetail.friend.AddFriend
import com.android.aschat.feature_host.domain.model.hostdetail.friend.CancelFriend
import com.android.aschat.feature_host.domain.model.hostdetail.userinfo.HostInfo
import com.android.aschat.feature_host.domain.repo.HostRepo
import com.android.aschat.feature_rongyun.rongyun.common.NowHost
import com.android.aschat.util.JsonUtil
import com.android.aschat.util.SpConstants
import com.android.aschat.util.SpUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rong.imkit.utils.RouteUtils
import io.rong.imlib.model.Conversation
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class HostViewModel @Inject constructor(@Named("HostRepo") private val repo: HostRepo, @Named("Context") val context: Context) : ViewModel() {

    // 主播详细资料界面

    // 当前查看的主播 NOTE 这个字段已经没有功能了，单纯地提供userId而已
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
     * 礼物map
     */
    private val _giftMap: MutableLiveData<Map<Gift, String>> = MutableLiveData(mutableMapOf())
    val giftMap: LiveData<Map<Gift, String>> = _giftMap

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

    /**
     * 是否屏蔽
     */
    private val _blocked: MutableLiveData<Boolean> = MutableLiveData(false)
    val blocked: LiveData<Boolean> = _blocked

    init {
        _hostData.observeForever { hostData ->
            viewModelScope.launch {
                supervisorScope {
                    launch {
                        val response = repo.getHostInfo(hostData.userId)
                        if (response.code == 0) {
                            // 成功
                            val hostInfo = response.data
                            _hostInfo.postValue(hostInfo)
                            _follow.postValue(hostInfo!!.isFriend)
                            _blocked.postValue(hostInfo.isBlock)
                        }else {
                            // 失败
                        }
                    }
                    launch {
                        val response = repo.getExtraInfo(hostData.userId)
                        if (response.code == 0) {
                            // 成功
                            // 设置标签
                            _labelList.postValue(response.data!!.labelsList)
                            // 设置礼物
                            _giftMap.postValue(giftAndNumList2Map(response.data.giftList))
                        }else {
                            // 失败
                        }
                    }
                    launch {
                        val response = repo.getUserStatus(hostData.userId)
                        if (response.code == 0) {
                            // 成功
                            // 更新_status
                            _status.postValue(response.data)
                        }else {
                            // 失败
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: HostEvents) {
        when (event) {
            is HostEvents.SendHostData -> {
                // 更改数据
                NowHost.hostId = event.hostData.userId
                _hostData.postValue(event.hostData)
            }
            is HostEvents.SendFriendData -> {
                NowHost.hostId = event.friendData.userId
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
            is HostEvents.SendBlockData -> {
                NowHost.hostId = event.blockData.broadcasterId
                val blockItem = event.blockData
                val hostData = HostData(
                    age = blockItem.age,
                    applicableTags = emptyList(),
                    avatar = blockItem.avatar,
                    avatarMapPath = blockItem.avatar,
                    callCoins = 0,
                    country = blockItem.registerCountry,
                    followNum = 0,
                    gender = blockItem.gender,
                    isFriend = true,
                    isMultiple = false,
                    nickname = blockItem.nickName,
                    status = "",
                    unit = "min",
                    userId = blockItem.broadcasterId,
                    videoMapPaths = emptyList()
                )
                _hostData.postValue(hostData)
            }
            is HostEvents.SendUserId -> {
                val userId = event.userId
                NowHost.hostId = userId
                val hostData = HostData(
                    age = 0,
                    applicableTags = emptyList(),
                    avatar = "",
                    avatarMapPath = "",
                    callCoins = 0,
                    country = "",
                    followNum = 0,
                    gender = 2,
                    isFriend = true,
                    isMultiple = false,
                    nickname = "",
                    status = "",
                    unit = "min",
                    userId = userId,
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
                         if (response.code == 0 && response.data!!) {
                             _follow.postValue(false)
                             Toast.makeText(context, context.getString(R.string.unfollow_success), Toast.LENGTH_SHORT).show()
                         }
                     }else {
                         // 原来没有关注，现在关注
                         val response = repo.addFriend(AddFriend(_hostInfo.value?.userId?:""))
                         if (response.code == 0 && response.data!!) {
                             _follow.postValue(true)
                             Toast.makeText(context, context.getString(R.string.follow_success), Toast.LENGTH_SHORT).show()
                         }
                     }
                 }
            }
            is HostEvents.ClickBlock -> {
                // 屏蔽或取消屏蔽主播
                viewModelScope.launch {
                    if (_blocked.value!!) {
                        // 原来屏蔽，现在取消屏蔽
                        val response = repo.cancelBlock(CancelBlock(_hostInfo.value!!.userId.toLong()))
                        if (response.code == 0 && response.data == true) {
                            _blocked.postValue(false)
                            Toast.makeText(context, context.getString(R.string.Unblock_successfully), Toast.LENGTH_SHORT).show()
                        }
                    }else {
                        // 原来没有屏蔽，现在屏蔽
                        val response = repo.blockOrReport(BlockOrReport(_hostInfo.value!!.userId.toLong(), Constants.Block))
                        if (response.code == 0 && response.data == true) {
                            _blocked.postValue(true)
                            Toast.makeText(context, context.getString(R.string.Block_successfully), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            is HostEvents.ClickReport -> {
                // 举报主播
                viewModelScope.launch {
                    val response = repo.blockOrReport(BlockOrReport(_hostInfo.value!!.userId.toLong(), Constants.Report, event.subtag))
                    if (response.code == 0 && response.data == true) {
                        _blocked.postValue(true)
                        Toast.makeText(context, context.getString(R.string.Report_successfully), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private suspend fun giftAndNumList2Map(giftAndNumList: List<String>): Map<Gift, String> {
        val map = mutableMapOf<Gift, String>()
        // 往map中写数据
        giftAndNumList.forEach {
            giftAndNum2Gift(map, it)
        }
        // 获取礼物策略数据
        val giftStrategy = getGiftStrategy()
        // 对map中的数据进行排序
        val sortedMap = map.toSortedMap { o1, o2 ->
            val order1 = giftStrategy.indexOfFirst {
                giftStr2Gift(it.code) == o1
            }
            val order2 = giftStrategy.indexOfFirst {
                giftStr2Gift(it.code) == o2
            }
            return@toSortedMap order1 - order2
        }
        return sortedMap
    }

    /**
     * 获取gift info
     */
    private suspend fun getGiftStrategy(): List<GiftInfo>{
        val giftStrategyStr = SpUtil.get(context, SpConstants.GIFT_STRATEGY, "") as String
        var giftStrategy: List<GiftInfo>? = null
        if (giftStrategyStr.isEmpty()) {
            var data: List<GiftInfo> = repo.getGiftInfo().data!!
            // 排序，数值越大越靠前
            data = data.sortedWith { o1, o2 ->
                if (o1.sortNo - o2.sortNo > 0) return@sortedWith -1
                else return@sortedWith 1
            }
            // 存好数据
            SpUtil.putAndApply(context, SpConstants.GIFT_STRATEGY, JsonUtil.any2Json(data))
            giftStrategy = data
        }else {
            giftStrategy = JsonUtil.json2Any(giftStrategyStr, List::class.java, GiftInfo::class.java)
        }
        return giftStrategy!!
    }
}

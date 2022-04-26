package com.android.aschat.feature_home.presentation

import android.content.Context
import android.content.Intent
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.*
import com.android.aschat.R
import com.android.aschat.common.Constants
import com.android.aschat.feature_home.domain.model.blocked.BlockedItem
import com.android.aschat.feature_home.domain.model.follow.FollowFriend
import com.android.aschat.feature_home.domain.model.follow.GetFriendList
import com.android.aschat.feature_home.domain.model.mine.EditDetail
import com.android.aschat.feature_home.domain.model.mine.HomeUserListItem
import com.android.aschat.feature_home.domain.repo.HomeRepo
import com.android.aschat.feature_home.domain.rv.ListState
import com.android.aschat.feature_host.presentation.HostActivity
import com.android.aschat.feature_login.domain.model.coin.CoinGood
import com.android.aschat.feature_login.domain.model.coin.CoinGoodPromotion
import com.android.aschat.feature_login.domain.model.login.UserInfo
import com.android.aschat.feature_login.domain.model.strategy.BroadcasterWallTag
import com.android.aschat.feature_login.domain.model.strategy.StrategyData
import com.android.aschat.util.*
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class HomeViewModel @Inject constructor(@Named("HomeRepo") private val repo: HomeRepo, @Named("Context") val context: Context) : ViewModel() {

    // 个人资料界面------

    private val _userInfo: MutableLiveData<UserInfo> = MutableLiveData(JsonUtil.json2Any(SpUtil.get(context, SpConstants.USERINFO, "") as String, UserInfo::class.java))
    val userInfo: LiveData<UserInfo> = _userInfo

    val avatarUrl: LiveData<String> = Transformations.map(_userInfo){
        it.avatarUrl
    }
    val coin: LiveData<String> = Transformations.map(_userInfo) {
        it.availableCoins.toString()
    }
    val nickName: LiveData<String> = Transformations.map(_userInfo) {
        it.nickname
    }

    private val _userItemList: MutableLiveData<List<HomeUserListItem>> = MutableLiveData(
        mutableListOf(
            HomeUserListItem(imageId = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_coin, null), text = context.getString(R.string.coin), cornText = _userInfo.value!!.availableCoins.toString()) {
                  it.navigate(R.id.action_userFragment_to_coinFragment)
            },
            HomeUserListItem(imageId = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_customer, null), text = context.getString(R.string.custom_services)) {
                //
            },
            HomeUserListItem(imageId = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_blocked, null), text = context.getString(R.string.block_list)) {
                  it.navigate(R.id.action_userFragment_to_blockFragment)
            },
            HomeUserListItem(imageId = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_about, null), text = context.getString(R.string.about)) {
                //
            },
            HomeUserListItem(imageId = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_setting, null), text = context.getString(R.string.setting)) {
                it.navigate(R.id.action_userFragment_to_settingFragment)
            }
        )
    )
    val userItemList: LiveData<List<HomeUserListItem>> = _userItemList

    // 个人资料编辑页------

    private val _userEditDetail: MutableLiveData<EditDetail> = MutableLiveData(
        EditDetail()
    )
    val userEditDetail: LiveData<EditDetail> = _userEditDetail

    // 主播墙界面------

    // 策略数据
    val wallStrategyData: StrategyData = JsonUtil.json2Any(SpUtil.get(context, SpConstants.STRATEGY, "") as String, StrategyData::class.java)

    // tags
    val parentTagList: List<BroadcasterWallTag> = wallStrategyData.broadcasterWallTagList

    // 关注页------
    private val _friends: MutableLiveData<MutableList<FollowFriend>> = MutableLiveData(mutableListOf())
    val friends: LiveData<MutableList<FollowFriend>> = _friends

    var friendsLimit = Constants.Follow_LimitPlus

    var followListState = ListState.REPLACE

    // 金币商店-----
    private val _coinGoods: MutableLiveData<List<CoinGood>> = MutableLiveData(mutableListOf())
    val coinGoods: LiveData<List<CoinGood>> = _coinGoods

    private val _timer: Timer = Timer()
    private val _timerTask: TimerTask = object :TimerTask() {
        override fun run() {
            val temp = _coinGoods.value
            // 一开始列表为空不要去影响原值
            if (temp!!.isEmpty()) return
            var hasPromotion = false
            temp.forEach {
                // 只会有一个item满足
                if (it.isPromotion) {
                    it.surplusMillisecond = it.surplusMillisecond - (System.currentTimeMillis() - SpUtil.get(context, SpConstants.COIN_GOODS_PROMOTION_TEMP_STAMP, 0L) as Long)
                    hasPromotion = true
                }
            }
            // 若是不更改，就不更新了
            if (hasPromotion) _coinGoods.postValue(temp) else return
        }
    }

    // 屏蔽页-----
    private val _blockList: MutableLiveData<List<BlockedItem>> = MutableLiveData(mutableListOf())
    val blockList: LiveData<List<BlockedItem>> = _blockList

    init {
        // 给coin增加监听，改变时修改rv(注：因为userinfo有其他信息，不想其他无所谓信息改变的时候更改userItemList)
        coin.observeForever {
            _userItemList.postValue(_userItemList.value.apply {
                this!![0].cornText = it
            })
        }
    }

    fun onEvent(event: HomeEvents) {
        when (event) {
            is HomeEvents.ToEditFragment -> {
                event.navController.navigate(R.id.action_userFragment_to_userEditFragment)
            }
            is HomeEvents.ShowTimePicker -> {
                PickerUtil.showTimePicker(event.fm) {datePickerDialog: DatePickerDialog, y: Int, m: Int, d: Int ->
                    _userEditDetail.postValue(_userEditDetail.value!!.copy(birthday = "$y-$m-$d"))
                }
            }
            is HomeEvents.ShowCountryPicker -> {
                PickerUtil.showCountryPicker(context = context, fm = event.fm) { name,  code,  dialCode,  flagDrawableResID ->
                    _userEditDetail.postValue(_userEditDetail.value!!.copy(country = name))
                }
            }
            is HomeEvents.ChangeHead -> {
                _userEditDetail.postValue(_userEditDetail.value!!.copy(head = event.head))
            }
            is HomeEvents.ExitUserEditFragment -> {
                event.navController.popBackStack()
            }
            is HomeEvents.ClickHost -> {
                val hostData = event.hostData
                val intent = Intent(context, HostActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    putExtra(Constants.WhereFrom, Constants.FromWall)
                    putExtra("hostData", JsonUtil.any2Json(hostData))
                }
                context.startActivity(intent)
            }
            // 关注页------
            is HomeEvents.FollowWantInit -> {
                viewModelScope.launch {
                    followListState = ListState.REPLACE
                    if (_friends.value!!.isEmpty()) {
                        // 数据为空就获取数据
                        friendsLimit = Constants.Follow_LimitPlus
                        val data = repo.getFriendList(GetFriendList(friendsLimit, 1)).data
                        val temp = data!!.toMutableList()
                        sortFriends(temp)
                        _friends.postValue(temp)
                    }else {
                        // 数据不为空就什么也不做

                    }
                }
            }
            is HomeEvents.FollowWantRefresh -> {
                viewModelScope.launch {
                    followListState = ListState.REPLACE
                    friendsLimit = Constants.Follow_LimitPlus
                    val data = repo.getFriendList(GetFriendList(friendsLimit, 1)).data
                    val temp = data!!.toMutableList()
                    sortFriends(temp)
                    // 覆盖数据
                    _friends.postValue(temp)
                }
            }
            is HomeEvents.FollowWantMore -> {
                viewModelScope.launch {
                    followListState = ListState.ADD
                    // 获取更多数据
                    friendsLimit += Constants.Follow_LimitPlus
                    val data = repo.getFriendList(GetFriendList(friendsLimit, 1)).data
                    // 旧数据
                    var list = _friends.value
                    // 增加数据
                    list!!.addAll(data!!)
                    // TODO: 为什么去重会有问题，在这里去重 
//                    list = list.distinctBy {
//                        it.userId
//                    }.toMutableList()
                    sortFriends(list)
                    // 提交数据
                    _friends.postValue(list)
                }
            }
            is HomeEvents.ClickFriend -> {
                val friend = event.friend
                val intent = Intent(context, HostActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    putExtra(Constants.WhereFrom, Constants.FromFollow)
                    putExtra("friendData", JsonUtil.any2Json(friend))
                }
                context.startActivity(intent)
            }

            is HomeEvents.LoadCoin -> {
                val str1 = SpUtil.get(context, SpConstants.COIN_GOODS, "") as String
                val str2 = SpUtil.get(context, SpConstants.COIN_GOODS_PROMOTION, "") as String
                val coinGoodList: MutableList<CoinGood> = JsonUtil.json2Any(str1, List::class.java, CoinGood::class.java)
                val coinGoodPromotion: CoinGoodPromotion = JsonUtil.json2Any(str2, CoinGoodPromotion::class.java)
                var hasPromotion = false
                // 把促销商品的倒计时赋值给对应的商品
                coinGoodList.forEach {
                    if (it.isPromotion) {
                        it.surplusMillisecond = coinGoodPromotion.surplusMillisecond.toLong()
                        hasPromotion = true
                    }
                }
                if (!hasPromotion) {
                    if (coinGoodPromotion.code.isNotEmpty()) {
                        // 虽然coinGoods没有促销商品，却有一个独立的促销商品
                        coinGoodList.add(
                            coinGoodPromotion.toCoinGood()
                        )
                        hasPromotion = true
                    }
                }
                _coinGoods.postValue(coinGoodList)

                // 有需要计算倒计时的项目，就启动计时器, 1 s执行一次
                if (hasPromotion)
                    _timer.schedule(_timerTask, 0L, 1000L)
            }

            is HomeEvents.EndTimer -> {
                _timerTask.cancel()
            }

            is HomeEvents.ExitCoinGoods -> {
                event.navController.popBackStack()
            }

            is HomeEvents.LoadBlockList -> {
                // TODO: 按照屏蔽时间倒序排列 
                viewModelScope.launch {
                    val response = repo.getBlockedList()
                    if (response.code == 0) {
                        // 成功
                        _blockList.postValue(response.data)
                    }
                }
            }

            is HomeEvents.ClickBlockItem -> {
                val blockItem = event.blockedItem
                val intent = Intent(context, HostActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    putExtra(Constants.WhereFrom, Constants.FromBlock)
                    putExtra("blockData", JsonUtil.any2Json(blockItem))
                }
                context.startActivity(intent)
            }

            is HomeEvents.ExitBlock -> {
                event.navController.popBackStack()
            }

            is HomeEvents.ExitSetting -> {
                event.navController.popBackStack()
            }
        }
    }

    /**
     * 对关注的用户进行一个排序
     *
     * 首先按状态，其次按首字母
     * 按照在线状态（在线>通话中>忙碌>离线），其次按首字母
     */
    private fun sortFriends(friends: MutableList<FollowFriend>) {
        friends.sortWith { o1, o2 ->
            if (o1.onlineStatus == Constants.Online && o2.onlineStatus != Constants.Online) {
                return@sortWith -1
            }
            if (o2.onlineStatus == Constants.Online && o1.onlineStatus != Constants.Online) {
                return@sortWith 1
            }
            if (o1.onlineStatus == Constants.Incall && o2.onlineStatus != Constants.Incall) {
                return@sortWith -1
            }
            if (o2.onlineStatus == Constants.Incall && o1.onlineStatus != Constants.Incall) {
                return@sortWith 1
            }
            if (o1.onlineStatus == Constants.Busy && o2.onlineStatus != Constants.Busy) {
                return@sortWith -1
            }
            if (o2.onlineStatus == Constants.Busy && o1.onlineStatus != Constants.Busy) {
                return@sortWith 1
            }
            if (o1.onlineStatus == Constants.Offline && o2.onlineStatus != Constants.Offline) {
                return@sortWith -1
            }
            if (o2.onlineStatus == Constants.Offline && o1.onlineStatus != Constants.Offline) {
                return@sortWith 1
            }
            return@sortWith -o1.nickname.compareTo(o2.nickname)
        }
    }
}
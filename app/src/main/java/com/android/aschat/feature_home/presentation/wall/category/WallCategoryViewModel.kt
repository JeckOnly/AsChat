package com.android.aschat.feature_home.presentation.wall.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.aschat.common.Constants
import com.android.aschat.feature_home.domain.model.wall.subtag.GetHostInfo
import com.android.aschat.feature_home.domain.model.wall.subtag.HostData
import com.android.aschat.feature_home.domain.repo.HomeRepo
import com.android.aschat.feature_home.domain.rv.ListState
import com.android.aschat.feature_login.domain.model.strategy.BroadcasterWallTag
import com.android.aschat.util.LogUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named


@HiltViewModel
class WallCategoryViewModel @Inject constructor(@Named("HomeRepo") private val repo: HomeRepo): ViewModel() {

    /**
     * 保存当前category的所有tag的list
     */
    private lateinit var mTagHostsMap: MutableMap<String, MutableList<HostData>>

    /**
     * 保存当前tag已加载的条数
     */
    private lateinit var mTagLimitMap: MutableMap<String, Int>

    /**
     * 当前要展示的list
     */
    private val _nowTagHosts: MutableLiveData<MutableList<HostData>> = MutableLiveData(mutableListOf())
    val nowTagHosts: LiveData<MutableList<HostData>> = _nowTagHosts

    /**
     * 当前category的信息
     */
    private lateinit var mBroadcasterWallTag: BroadcasterWallTag

    /**
     * 当前tag
     */
    private var mTag: String = "All"

    /**
     * 标志当前的list是全新的，还是旧列表有新数据加在后面
     *
     * 0表示全新，1表示新数据在后面
     */
    var mListState: ListState = ListState.REPLACE
    private set

    /**
     * 存储用户id和上次刷新时间的map
     */
    private val mHostRefreshMap: MutableMap<String, Long> = mutableMapOf()

    fun onEvent(event: SubTagEvents) {
        when(event) {
            is SubTagEvents.InitSubTags -> {
                // 设置category的信息
                mBroadcasterWallTag = event.broadcasterWallTag
               // 初始化map，让列表为空先
               mTagHostsMap = mutableMapOf<String, MutableList<HostData>>().apply {
                   mBroadcasterWallTag.subTagList.forEach { subTag ->
                       this[subTag] = mutableListOf()
                   }
               }
               mTagLimitMap = mutableMapOf<String, Int>().apply {
                   mBroadcasterWallTag.subTagList.forEach { subTag ->
                       this[subTag] = Constants.HostWall_LimitPlus
                   }
               }
            }

            is SubTagEvents.ChangeTab -> {
                // 更改当前要展示的列表
                viewModelScope.launch {
                    mTag = event.tag
                    val tempList = mTagHostsMap[mTag]
                    if (tempList!!.isEmpty()) {
                        // 如果还没有加载过就加载这个subtag下面的数据，然后再赋值给_nowTagHosts
                        val newHostData: List<HostData> = repo.getHostData(
                            GetHostInfo(
                                category = mBroadcasterWallTag.tagName,
                                isRemoteImageUrl = true,
                                limit = Constants.HostWall_LimitPlus,
                                page = 1,
                                tag = mTag)
                        ).data!!
                        mTagHostsMap[mTag] = newHostData.toMutableList()
                        // 全部替换了
                        mListState = ListState.REPLACE
                        _nowTagHosts.postValue(mTagHostsMap[mTag])
                    }else {
                        // 全部替换了，用原来存好的数据就行
                        mListState = ListState.REPLACE
                        _nowTagHosts.postValue(mTagHostsMap[mTag])
                    }
                }
            }

            is SubTagEvents.WantRefresh -> {
                // 刷新数据
                viewModelScope.launch {
                    // 恢复limit
                    mTagLimitMap[mTag] = Constants.HostWall_LimitPlus
                    mListState = ListState.REPLACE
                    // 取新data
                    val newHostData: List<HostData> = repo.getHostData(
                        GetHostInfo(
                            category = mBroadcasterWallTag.tagName,
                            isRemoteImageUrl = true,
                            limit = Constants.HostWall_LimitPlus,
                            page = 1,
                            tag = mTag)
                    ).data!!
                    // 覆盖旧数据
                    mTagHostsMap[mTag] = newHostData.toMutableList()
                    _nowTagHosts.postValue(mTagHostsMap[mTag])
                }
            }

            is SubTagEvents.WantMore -> {
                // 增加数据
                viewModelScope.launch {
                    // 增加limit
                    mTagLimitMap[mTag] = mTagLimitMap[mTag]!! + Constants.HostWall_LimitPlus
                    mListState = ListState.ADD
                    // 请求数据
                    val newHostData: List<HostData> = repo.getHostData(
                        GetHostInfo(
                            category = mBroadcasterWallTag.tagName,
                            isRemoteImageUrl = true,
                            limit = mTagLimitMap[mTag]!!,
                            page = 1,
                            tag = mTag)
                    ).data!!
                    // 加在旧数据后面
                    mTagHostsMap[mTag]!!.apply {
                        addAll(newHostData)

//                        forEachIndexed { index, hostData ->
//                            LogUtil.d("$index ${hostData.userId}")
//                        }
                    }
                    // 去重， 留给rv去做
//                    mTagHostsMap[mTag] = mTagHostsMap[mTag]!!.distinctBy {
//                        it.userId
//                    }.toMutableList()
                    _nowTagHosts.postValue(mTagHostsMap[mTag]!!)
                }
            }

            is SubTagEvents.RefreshHost -> {
                viewModelScope.launch {
                    val startIndex = event.startIndex
                    val endIndex = event.endIndex
                    var idList = mutableListOf<String>()
                    // 得到需要更新的userId的集合
                    for (i in startIndex..endIndex) {
                        try {
                            idList.add(mTagHostsMap[mTag]!![i].userId)
                        } catch (e: ArrayIndexOutOfBoundsException) {
                            LogUtil.e("WallCategoryViewModel数组越界")
                        } catch (e: Exception) {

                        }
                    }
                    idList = idList.filter {
                        checkNeedRefresh(it)
                    }.toMutableList()
                    // NOTE 返回的userId的map是无序的
                    val response = repo.getUserStatusList(idList)
                    if (response.code == 0) {
                        val statusMap = response.data
                        statusMap!!.forEach { entry ->
                            val userId = entry.key
                            val status = entry.value
                            // 找出第一个满足的userId然后更新状态
                            mTagHostsMap[mTag]!!.first { hostData ->
                                hostData.userId == userId
                            }.status = status
                            // 更新时间戳
                            updateHostRefreshTimeStamp(userId)
                        }
                        event.doAfterGetNewData()
                    }
//                    // NOTE 模拟数据改变
//                    mTagHostsMap[mTag]!!.apply {
//                        for (i in startIndex..endIndex) {
//                            get(i).nickname = "xiejunyan"
//                        }
//                    }
                }
            }
        }
    }

    /**
     * 判断当前id对应的主播是否需要刷新
     *
     * 如果当前已存在主播的时间戳，就判断是否 > 10s
     * 不存在就返回true
     * @return true表示需要刷新，false不需要刷新
     */
    private fun checkNeedRefresh(hostId: String): Boolean {
        if (mHostRefreshMap.containsKey(hostId)) {
            return System.currentTimeMillis() - mHostRefreshMap[hostId]!!.toLong() > Constants.Host_Refresh_Stamp
        }else {
            return true
        }
    }

    /**
     * 把当前主播刷新的时间戳更新 NOTE 没有key会写入
     */
    private fun updateHostRefreshTimeStamp(hostId: String) {
        mHostRefreshMap[hostId] = System.currentTimeMillis()
    }
}

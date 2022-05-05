package com.android.aschat.feature_rank.presentation

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.aschat.common.Constants
import com.android.aschat.feature_host.domain.model.hostdetail.userinfo.HostInfo
import com.android.aschat.feature_host.presentation.HostActivity
import com.android.aschat.feature_rank.domain.model.GetRankData
import com.android.aschat.feature_rank.domain.model.charm.RankCharmData
import com.android.aschat.feature_rank.domain.model.rich.RankRichData
import com.android.aschat.feature_rank.domain.repo.RankRepo
import com.android.aschat.util.JsonUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class RankViewModel @Inject constructor(
    @Named("RankRepo") private val repo: RankRepo
): ViewModel() {

    private val _rankCharmData: MutableLiveData<RankCharmData> = MutableLiveData()
    val rankCharmData: LiveData<RankCharmData> = _rankCharmData

    private val _rankRichData: MutableLiveData<RankRichData> = MutableLiveData()
    val rankRichData: LiveData<RankRichData> = _rankRichData

    private val _userInfoMoreDetailed: MutableLiveData<HostInfo> = MutableLiveData(HostInfo())
    val userInfoMoreDetailed: LiveData<HostInfo> = _userInfoMoreDetailed

    fun onEvent(event: RankEvents) {
        when (event) {
            is RankEvents.ExitRankMainFragment -> {
                event.activity.finish()
            }

            is RankEvents.InitHostInfo -> {
                _userInfoMoreDetailed.postValue(event.hostInfo)
            }

            is RankEvents.ClickHost -> {
                val intent = Intent(event.context, HostActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    putExtra(Constants.WhereFrom, Constants.FromRank)
                    putExtra("userId", event.userId)
                }
                event.context.startActivity(intent)
            }

            is RankEvents.LoadRankCharmData -> {
                viewModelScope.launch {
                    // 获取主播榜数据
                    val response = repo.getRankCharmData(GetRankData(Constants.Rank_Count))
                    if (response.code == 0) {
                        val data = response.data
                        if (data!!.rankData.size > Constants.Rank_Count) {
                            data.rankData = data.rankData.subList(0, Constants.Rank_Count)
                        }
                        _rankCharmData.postValue(response.data)
                    }
                }
            }

            is RankEvents.LoadRankRichData -> {
                viewModelScope.launch {
                    // 获取用户榜数据
                    val response = repo.getRankRichData(GetRankData(Constants.Rank_Count))
                    if (response.code == 0) {
                        val data = response.data
                        if (data!!.rankData.size > Constants.Rank_Count) {
                            data.rankData = data.rankData.subList(0, Constants.Rank_Count)
                        }
                        _rankRichData.postValue(response.data)
                    }
                }
            }
        }
    }
}
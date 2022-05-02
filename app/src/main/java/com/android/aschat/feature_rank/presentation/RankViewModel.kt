package com.android.aschat.feature_rank.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.aschat.common.Constants
import com.android.aschat.feature_rank.domain.model.GetRankData
import com.android.aschat.feature_rank.domain.model.charm.RankCharmData
import com.android.aschat.feature_rank.domain.model.rich.RankRichData
import com.android.aschat.feature_rank.domain.repo.RankRepo
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

    fun onEvent(event: RankEvents) {
        when (event) {
            is RankEvents.ExitRankMainFragment -> {
                event.activity.finish()
            }

            is RankEvents.LoadRankCharmData -> {
                viewModelScope.launch {
                    // 获取主播榜数据
                    val response = repo.getRankCharmData(GetRankData(Constants.Rank_Count))
                    if (response.code == 0) {
                        val data = response.data
                        if (data!!.rankCharmItems.size > Constants.Rank_Count) {
                            data.rankCharmItems = data.rankCharmItems.subList(0, Constants.Rank_Count)
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
                        if (data!!.rankRichItems.size > Constants.Rank_Count) {
                            data.rankRichItems = data.rankRichItems.subList(0, Constants.Rank_Count)
                        }
                        _rankRichData.postValue(response.data)
                    }
                }
            }
        }
    }
}
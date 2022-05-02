package com.android.aschat.feature_rank.domain.repo

import com.android.aschat.common.network.AppServices
import com.android.aschat.common.network.Response
import com.android.aschat.feature_host.domain.model.hostdetail.extrainfo.GiftInfo
import com.android.aschat.feature_login.data.UserDao
import com.android.aschat.feature_login.domain.model.appconfig.ConfigList
import com.android.aschat.feature_login.domain.model.coin.CoinGood
import com.android.aschat.feature_login.domain.model.coin.CoinGoodPromotion
import com.android.aschat.feature_login.domain.model.coin.GetCoinGood
import com.android.aschat.feature_login.domain.model.login.LoginData
import com.android.aschat.feature_login.domain.model.login.UserInfo
import com.android.aschat.feature_login.domain.model.osspolicy.OssPolicy
import com.android.aschat.feature_login.domain.model.strategy.StrategyData
import com.android.aschat.feature_rank.domain.model.GetRankData
import com.android.aschat.feature_rank.domain.model.charm.RankCharmData
import com.android.aschat.feature_rank.domain.model.rich.RankRichData
import com.android.aschat.util.LogUtil

class RankRepo(private val services: AppServices) {

    suspend fun getRankCharmData(getRankData: GetRankData): Response<RankCharmData> {
        val response = services.getRankCharmData(getRankData)
        return response
    }

    suspend fun getRankRichData(getRankData: GetRankData): Response<RankRichData> {
        val response = services.getRankRichData(getRankData)
        return response
    }
}
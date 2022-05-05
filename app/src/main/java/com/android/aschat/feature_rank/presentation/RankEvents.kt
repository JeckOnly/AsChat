package com.android.aschat.feature_rank.presentation

import android.content.Context
import androidx.navigation.NavController
import com.android.aschat.feature_host.domain.model.hostdetail.userinfo.HostInfo

sealed class RankEvents{
    class ExitRankMainFragment(val activity: RankActivity): RankEvents()
    class InitHostInfo(val hostInfo: HostInfo): RankEvents()
    object LoadRankCharmData: RankEvents()
    object LoadRankRichData: RankEvents()
    class ClickHost(val context: Context, val userId: String): RankEvents()
}

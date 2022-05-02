package com.android.aschat.feature_rank.presentation

import android.content.Context
import androidx.navigation.NavController

sealed class RankEvents{
    class ExitRankMainFragment(val activity: RankActivity): RankEvents()
    object LoadRankCharmData: RankEvents()
    object LoadRankRichData: RankEvents()
}

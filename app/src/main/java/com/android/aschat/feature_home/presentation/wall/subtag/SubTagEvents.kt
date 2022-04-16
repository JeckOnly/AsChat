package com.android.aschat.feature_home.presentation.wall.subtag

import com.android.aschat.feature_home.domain.model.wall.subtag.GetHostInfo
import com.android.aschat.feature_login.domain.model.strategy.BroadcasterWallTag


/**
 * 和一个category有关的事件都在这里
 */
sealed class SubTagEvents {
    data class InitSubTags(val broadcasterWallTag: BroadcasterWallTag): SubTagEvents()
    data class ChangeTab(val tag: String): SubTagEvents()
    object WantMore: SubTagEvents()
    object WantRefresh: SubTagEvents()
}
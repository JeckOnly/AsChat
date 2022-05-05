package com.android.aschat.feature_rank.domain.model.charm

data class RankCharmData(
    val monthName: String = "",
    var rankData: List<RankCharmItem> = emptyList(),
    val sortNo: String = ""
)
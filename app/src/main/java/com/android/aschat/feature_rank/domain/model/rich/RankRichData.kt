package com.android.aschat.feature_rank.domain.model.rich

data class RankRichData(
    val monthName: String = "",
    var rankRichItems: List<RankRichItem> = emptyList(),
    val sortNo: String = ""
)
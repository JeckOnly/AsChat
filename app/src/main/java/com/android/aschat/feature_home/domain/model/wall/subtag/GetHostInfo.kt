package com.android.aschat.feature_home.domain.model.wall.subtag

data class GetHostInfo(
    val category: String,
    val isRemoteImageUrl: Boolean,
    val limit: Int,
    val page: Int,
    val tag: String
)
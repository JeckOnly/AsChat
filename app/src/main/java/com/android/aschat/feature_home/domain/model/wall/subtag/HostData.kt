package com.android.aschat.feature_home.domain.model.wall.subtag

data class HostData(
    val age: Int,
    val applicableTags: List<String>,
    val avatar: String,
    val avatarMapPath: String,
    val callCoins: Int,
    val country: String,
    val followNum: Int,
    val gender: Int,
    val isFriend: Boolean,
    val isMultiple: Boolean,
    val nickname: String,
    val status: String,
    val unit: String,
    val userId: String,
    val videoMapPaths: List<String>
)
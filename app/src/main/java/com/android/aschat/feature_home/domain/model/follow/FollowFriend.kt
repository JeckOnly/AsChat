package com.android.aschat.feature_home.domain.model.follow

/**
 * 关注列表获得的实体
 */
data class FollowFriend(
    val about: String,
    val age: Int,
    val avatar: String,
    val avatarUrl: String,
    val birthday: String,
    val country: String,
    val gender: Int,
    val isSpecialFollow: Boolean,
    val isVip: Boolean,
    val language: String,
    val level: Int,
    val nickname: String,
    val onlineStatus: String,
    val userId: String,
    val userType: Int
)
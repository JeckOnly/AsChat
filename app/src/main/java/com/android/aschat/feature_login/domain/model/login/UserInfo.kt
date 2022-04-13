package com.android.aschat.feature_login.domain.model.login

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserInfo(
    val age: Int,
    val auditStatus: Int,
    val availableCoins: Int,
    val avatar: String,
    val avatarMiddleThumbUrl: String,
    val avatarThumbUrl: String,
    val avatarUrl: String,
    val birthday: String,
    val country: String,
    val followNum: Int,
    val gender: Int,
    val isAnswer: Boolean,
    val isBlock: Boolean,
    val isHavePassword: Boolean,
    val isInternal: Boolean,
    val isRecharge: Boolean,
    val isSwitchNotDisturbCall: Boolean,
    val isSwitchNotDisturbIm: Boolean,
    val isVip: Boolean,
    val level: Int,
    val nickname: String,
    val praiseNum: Int,
    val rongcloudToken: String,
    val tagDetails: List<TagDetail>,
    val tagsList: List<String>,
    @PrimaryKey val userId: String,
    val userType: Int
)
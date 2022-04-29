package com.android.aschat.feature_host.domain.model.hostdetail.userinfo

/**
 * 名字虽然叫host info，但也可以是用户的模型
 */
data class HostInfo(
    val about: String = "",
    val acceptMultipleCall: Boolean = false,
    val age: Int = 0,
    val auditStatus: Int = 0,
    val avatar: String = "",
    val avatarMiddleThumbUrl: String = "",
    val avatarThumbUrl: String = "",
    val avatarUrl: String = "",
    val birthday: String = "",
    val country: String = "",
    val followNum: Int = 0,
    val gender: Int = 0,
    val grade: Int = 0,
    val isAnswer: Boolean = false,
    val isBlock: Boolean = false,
    val isClub: Boolean = false,
    val isHavePassword: Boolean = false,
    val isInternal: Boolean = false,
    val isProbationPeriod: Boolean = false,
    val isRecharge: Boolean = false,
    val isSwitchNotDisturbCall: Boolean = false,
    val isSwitchNotDisturbIm: Boolean = false,
    val isVip: Boolean = false,
    val language: String = "",
    val level: Int = 0,
    val mediaList: List<Media> = emptyList(),
    val nickname: String = "",
    val praiseNum: Int = 0,
    val rongcloudToken: String = "",
    val tagDetails: List<Any> = emptyList(),
    val tagsList: List<Any> = emptyList(),
    val unitPrice: Int = 0,
    val userId: String = "",
    val userType: Int = 0,
    val vipUnitPrice: Int = 0,
    val isFriend: Boolean = false
)
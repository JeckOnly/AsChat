package com.android.aschat.feature_rank.domain.model.charm

data class RankCharmItem(
    val avatar: String = "",
    val avatarMapPath: String = "",
    val nickname: String = "",
    val sort: Int = 0,
    val totalIncomeCoins: Int = 0,
    val userId: String = "",
    val broadcasterId:Int = 0,
    val broadcasterName:String = "",
    val broadcasterOnlineTime:Int = 0,
    val guildId :Int = 0,
)
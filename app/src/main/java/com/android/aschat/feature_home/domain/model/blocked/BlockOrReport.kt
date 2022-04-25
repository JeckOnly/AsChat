package com.android.aschat.feature_home.domain.model.blocked

/**
 * 屏蔽或举报某人
 */
data class BlockOrReport(
    val broadcasterId: Long,
    val complainCategory: String,
    val complainSub: String = ""
)
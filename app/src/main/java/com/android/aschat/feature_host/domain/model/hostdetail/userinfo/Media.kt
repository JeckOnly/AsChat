package com.android.aschat.feature_host.domain.model.hostdetail.userinfo

data class Media(
    val coins: Int,
    val mediaId: String,
    val mediaPath: String,
    val mediaType: String,
    val mediaUrl: String,
    val middleThumbUrl: String,
    val sort: Int,
    val thumbUrl: String,
    val userId: String
)
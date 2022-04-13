package com.android.aschat.feature_home.domain.model

import android.graphics.drawable.Drawable

data class HomeUserListItem(
    val imageId: Drawable?,
    val text: String,
    var cornText: String = "",
    var clickListener: () -> Unit
)

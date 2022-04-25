package com.android.aschat.feature_home.domain.model.mine

import android.graphics.drawable.Drawable
import androidx.navigation.NavController

data class HomeUserListItem(
    val imageId: Drawable?,
    val text: String,
    var cornText: String = "",
    var clickListener: (NavController) -> Unit
)

package com.android.aschat.feature_login.presentation

import android.content.Context
import androidx.navigation.NavController


/**
 * 登录有关的界面的事件都写在这里
 */
sealed class LoginEvents{
    class LoginEvent(val context: Context, val navController: NavController): LoginEvents()
    class FastLogin2HomeActivity(val context: Context, val navController: NavController): LoginEvents()
}

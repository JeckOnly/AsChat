package com.android.aschat.common

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * 使用hilt需要加这个注释
 */
@HiltAndroidApp
class MyApplication :Application(){
}
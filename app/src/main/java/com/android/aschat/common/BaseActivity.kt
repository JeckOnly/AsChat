package com.android.aschat.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.aschat.R
import com.android.aschat.util.StatusBarUtil

abstract class BaseActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(provideLayoutId())
        // 设置状态栏纯色
        StatusBarUtil.setColorNoTranslucent(this, resources.getColor(R.color.pink1))
//         设置状态栏透明 无效
//        StatusBarUtil.setTransparent(this)
        init()
    }

    abstract fun provideLayoutId(): Int

    open fun init() {

    }
}
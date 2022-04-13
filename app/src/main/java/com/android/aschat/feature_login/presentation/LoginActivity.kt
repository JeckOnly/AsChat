package com.android.aschat.feature_login.presentation

import com.android.aschat.R
import com.android.aschat.common.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity: BaseActivity() {
    override fun init() {
        super.init()

    }

    override fun provideLayoutId(): Int {
        return R.layout.login_acty
    }
}
package com.android.aschat.feature_rank.presentation

import com.android.aschat.R
import com.android.aschat.common.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RankActivity: BaseActivity() {
    override fun init() {
        super.init()

    }

    override fun provideLayoutId(): Int {
        return R.layout.rank_acty
    }
}
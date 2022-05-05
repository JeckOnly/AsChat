package com.android.aschat.feature_rank.presentation

import androidx.activity.viewModels
import com.android.aschat.R
import com.android.aschat.common.BaseActivity
import com.android.aschat.feature_host.domain.model.hostdetail.userinfo.HostInfo
import com.android.aschat.feature_host.presentation.HostViewModel
import com.android.aschat.util.JsonUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RankActivity: BaseActivity() {
    private val mViewModel: RankViewModel by viewModels()

    override fun init() {
        super.init()
        val hostInfoStr = intent.getStringExtra("hostInfo")
        val hostInfo = JsonUtil.json2Any(hostInfoStr!!, HostInfo::class.java)
        mViewModel.onEvent(RankEvents.InitHostInfo(hostInfo))
    }

    override fun provideLayoutId(): Int {
        return R.layout.rank_acty
    }
}
package com.android.aschat.feature_host.presentation

import androidx.activity.viewModels
import com.android.aschat.R
import com.android.aschat.common.BaseActivity
import com.android.aschat.common.Constants
import com.android.aschat.feature_home.domain.model.follow.FollowFriend
import com.android.aschat.feature_home.domain.model.wall.subtag.HostData
import com.android.aschat.util.JsonUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HostActivity : BaseActivity() {

    private val mViewModel: HostViewModel by viewModels()

    override fun init() {
        super.init()
        val from = intent.getIntExtra(Constants.WhereFrom, -1)
        if (from == Constants.FromWall) {
            val hostData = JsonUtil.json2Any(intent.getStringExtra("hostData")!!, HostData::class.java)
            mViewModel.onEvent(HostEvents.SendHostData(hostData))
        }else if (from == Constants.FromFollow) {
            val friendData = JsonUtil.json2Any(intent.getStringExtra("friendData")!!, FollowFriend::class.java)
            mViewModel.onEvent(HostEvents.SendFriendData(friendData))
        }
    }

    override fun provideLayoutId(): Int {
        return R.layout.host_acty
    }
}
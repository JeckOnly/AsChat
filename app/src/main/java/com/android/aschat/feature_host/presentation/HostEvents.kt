package com.android.aschat.feature_host.presentation

import android.app.Activity
import androidx.navigation.NavController
import com.android.aschat.feature_home.domain.model.blocked.BlockedItem
import com.android.aschat.feature_home.domain.model.follow.FollowFriend
import com.android.aschat.feature_home.domain.model.wall.subtag.HostData
import com.android.aschat.feature_rongyun.rongyun.model.ExtraInfo

sealed class HostEvents {
    // 主播资料页

    class SendHostData(val hostData: HostData): HostEvents()
    class SendFriendData(val friendData: FollowFriend): HostEvents()
    class SendBlockData(val blockData: BlockedItem): HostEvents()
    class SendUserId(val userId: String): HostEvents()
    class ExitHostDetail(val activity: HostActivity): HostEvents()
    class JumpFullScreenImage(val navController: NavController, val position: Int): HostEvents()
    class JumpFullScreenVideo(val navController: NavController, val position: Int): HostEvents()
    class ExitFullScreenImage(val navController: NavController): HostEvents()
    class ExitFullScreenVideo(val navController: NavController): HostEvents()
    object ClickFollow : HostEvents()
    object ClickBlock : HostEvents()
    class ClickReport(val subtag: String = ""): HostEvents()

     // 主播聊天页
     class SubmitRecharge(val activity: Activity, val extraInfo: ExtraInfo?, val onStartSubmit: () -> Unit, val onSuccess: () -> Unit, val onFail: () -> Unit): HostEvents()
}
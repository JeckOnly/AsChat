package com.android.aschat.feature_home.presentation

import androidx.navigation.NavController
import com.android.aschat.feature_home.domain.model.blocked.BlockedItem
import com.android.aschat.feature_home.domain.model.follow.FollowFriend
import com.android.aschat.feature_home.domain.model.mine.EditDetail
import com.android.aschat.feature_home.domain.model.wall.subtag.HostData
import com.android.aschat.feature_login.domain.model.coin.CoinGood

sealed class HomeEvents {
    // 个人资料编辑页
    class SubmitEdit(val editDetail: EditDetail, val onStartSubmit: () -> Unit, val onSuccess: () -> Unit,  val onFail: () -> Unit): HomeEvents()
    class ToEditFragment(val navController: NavController): HomeEvents()
    class ExitUserEditFragment(val navController: NavController): HomeEvents()

    // 个人资料页
    // 点击头像
    class UploadImageToOss(val filePath: String, val onStartSubmit: () -> Unit, val onSuccess: () -> Unit,  val onFail: () -> Unit): HomeEvents()

    // 首页
    data class ClickHost(val hostData: HostData): HomeEvents()
    object ClickRank: HomeEvents()

    // 关注页
    object FollowWantInit: HomeEvents()
    object FollowWantRefresh: HomeEvents()
    object FollowWantMore: HomeEvents()
    class ClickFriend(val friend: FollowFriend): HomeEvents()

    // 金币商店页
    object LoadCoin: HomeEvents()
    class ClickCoinGood(val coinGood: CoinGood): HomeEvents()
    object EndTimer: HomeEvents()
    class ExitCoinGoods(val navController: NavController): HomeEvents()

    // 屏蔽页
    object LoadBlockList: HomeEvents()
    class ClickBlockItem(val blockedItem: BlockedItem): HomeEvents()
    class ExitBlock(val navController: NavController): HomeEvents()

    // 设置页
    class ExitSetting(val navController: NavController): HomeEvents()

    // 关于页
    class ExitAbout(val navController: NavController): HomeEvents()
}
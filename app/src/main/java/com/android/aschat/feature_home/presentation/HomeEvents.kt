package com.android.aschat.feature_home.presentation

import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import com.android.aschat.feature_home.domain.model.blocked.BlockedItem
import com.android.aschat.feature_home.domain.model.follow.FollowFriend
import com.android.aschat.feature_home.domain.model.mine.EditDetail
import com.android.aschat.feature_home.domain.model.wall.subtag.HostData
import com.android.aschat.feature_login.domain.model.coin.CoinGood

sealed class HomeEvents {
    // 个人资料编辑页
    class SubmitEdit(val navController: NavController, val editDetail: EditDetail): HomeEvents()
    class ToEditFragment(val navController: NavController): HomeEvents()
    class ShowTimePicker(val fm: FragmentManager): HomeEvents()
    class ShowCountryPicker(val fm: FragmentManager): HomeEvents()
    class ChangeHead(val head:Int): HomeEvents()
    class ExitUserEditFragment(val navController: NavController): HomeEvents()

    // 首页
    data class ClickHost(val hostData: HostData): HomeEvents()

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
}
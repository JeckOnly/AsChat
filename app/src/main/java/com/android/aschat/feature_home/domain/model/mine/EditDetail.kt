package com.android.aschat.feature_home.domain.model.mine

import com.android.aschat.common.Constants

data class EditDetail(
    var nickName: String = "",
    var birthday: String = Constants.Default_Birthday,
    var country: String = "",
    var inviteCode: String = "",
    var gender: Int = 1,// 1是男，2是女
    var head: Int = 0,// 1,2,3, 0表示不是本地图片，具体的本地图片结合gender判断
    var about: String = ""
)

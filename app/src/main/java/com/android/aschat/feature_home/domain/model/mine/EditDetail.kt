package com.android.aschat.feature_home.domain.model.mine

import com.android.aschat.common.Constants

data class EditDetail(
    var nickName: String = "",
    var birthday: String = Constants.Default_Birthday,
    var country: String = "",
    var inviteCode: String = "",// 这个字段暂时不用
    var about: String = "",
    var avatarSrcPath: String = ""// 照片的本地路径
)

package com.android.aschat.feature_home.domain.model.mine

import okhttp3.RequestBody

data class FileBody(
    var fileId: String,
    var fileName: String,
    var fileBody: RequestBody
)

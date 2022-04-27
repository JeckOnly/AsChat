package com.android.aschat.feature_login.domain.model.osspolicy

/**
 * oss 策略
 */
data class OssPolicy(
    val accessKeyId: String,
    val callback: String,
    val dir: String,
    val expire: String,
    val host: String,
    val policy: String,
    val signature: String
)
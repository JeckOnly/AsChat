package com.android.aschat.common.network

data class Response<T>(
    val code: Int,
    val data: T?,
    val key: String,
    val msg: String
)

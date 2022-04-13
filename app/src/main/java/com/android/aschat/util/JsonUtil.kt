package com.android.aschat.util

import com.google.gson.Gson
import com.android.aschat.common.database.ClassConverters

object JsonUtil {
    private val gson = Gson()

    fun any2Json(src: Any): String {
        return gson.toJson(src)
    }

    fun <T> json2Any(json: String, classType: Class<T>): T {
        return ClassConverters.gson.fromJson(json, classType)
    }
}
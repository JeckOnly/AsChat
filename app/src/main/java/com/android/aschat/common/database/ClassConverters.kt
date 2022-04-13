package com.android.aschat.common.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.android.aschat.feature_login.domain.model.login.TagDetail

class ClassConverters {
    @TypeConverter
    fun list_tagDetail_2_json(list: List<TagDetail>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun json_2_list_tagDetail(value: String): List<TagDetail> {
        val type = object : TypeToken<List<TagDetail>>() {

        }.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun list_str_2_json(list: List<String>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun json_2_list_str(value: String): List<String> {
        val type = object : TypeToken<List<String>>() {

        }.type
        return gson.fromJson(value, type)
    }



    companion object {
        val gson = Gson()
    }
}
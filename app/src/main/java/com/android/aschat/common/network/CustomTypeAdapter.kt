package com.android.aschat.common.network

import com.android.aschat.feature_login.domain.model.appconfig.*
import com.android.aschat.util.LogUtil
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

@Deprecated("这个不生效，用AppConfigDeserializer")
class AppConfigBaseDeserializer: JsonDeserializer<ConfigItemBase> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ConfigItemBase {
        val jsonObj = json!!.asJsonObject
        val type = jsonObj["type"].asString

        if (type == "translate_type") {
            return Gson().fromJson(json, ConfigItemStrInt::class.java)
        } else if (type == "anchor_netspeedcheck") {
            return Gson().fromJson(json, ConfigItemStrSpeedObject::class.java)
        } else  {
            return Gson().fromJson(json, ConfigItemStrStr::class.java)
        }
    }
}

class AppConfigDeserializer: JsonDeserializer<ConfigList> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ConfigList {
        try {
            val gson = Gson()
            // 总的json对象
            val jsonObj = json!!.asJsonObject
            // 先建一个空列表
            val itemList = mutableListOf<ConfigItemBase>()
            // 取出json中的列表
            val jsonArray = jsonObj["items"].asJsonArray
            // 循环
            for (item in jsonArray) {
                try {
                    val tempObj = item.asJsonObject
                    if (tempObj["name"].asString == "translate_type") {
                        itemList.add(gson.fromJson(item, ConfigItemStrInt::class.java))
                    }else if (tempObj["name"].asString == "anchor_netspeedcheck") {
                        itemList.add(gson.fromJson(item, ConfigItemStrSpeedObject::class.java))
                    }else if (tempObj["name"].asString == "upgrade") {
                        itemList.add(gson.fromJson(item, ConfigItemStrUpgradeObject::class.java))
                    }else {
                        itemList.add(gson.fromJson(item, ConfigItemStrStr::class.java))
                    }
                }catch (e: Exception) {
                    // NOTE 有item解析错误，说明后台加了新的实体，直接跳过解析错误的
                    LogUtil.e(item.toString())
                }
            }
            return ConfigList(itemList, jsonObj["ver"].asString)
        } catch (e: Exception) {
            // 能来到这里的报错，说明最外层data为”“
            return ConfigList(emptyList(), "0")
        }
    }
}
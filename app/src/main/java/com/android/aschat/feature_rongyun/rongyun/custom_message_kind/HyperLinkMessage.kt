package com.android.aschat.feature_rongyun.rongyun.custom_message_kind

import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import io.rong.common.ParcelUtils
import io.rong.common.RLog
import io.rong.imlib.MessageTag
import io.rong.imlib.model.MessageContent
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

@MessageTag(value = "LC:HyperLinkMsg", flag = MessageTag.ISCOUNTED)
class HyperLinkMessage() : MessageContent() {

    var content = ""// 文本内容
    var contentType = "" // 内容类型   "recharge_link" -> 充值链接消息
    var extraInfo = ""// 额外数据

    constructor(parcel: Parcel) : this() {
        content = ParcelUtils.readFromParcel(parcel)
        contentType = ParcelUtils.readFromParcel(parcel)
        extraInfo = ParcelUtils.readFromParcel(parcel)
    }

    constructor(data: ByteArray?) : this() {
        if (data == null) return

        var jsonStr: String? = null

        try {
            if (data.size >= 40960) {
                RLog.e(
                    "TextMessage",
                    "TextMessage length is larger than 40KB, length :" + data.size
                )
            }
            jsonStr = String(data, Charset.forName("UTF-8"))
        } catch (var5: UnsupportedEncodingException) {
            RLog.e("TextMessage", "UnsupportedEncodingException ", var5)
        }

        if (jsonStr == null) {
            RLog.e("TextMessage", "jsonStr is null ")
        } else {
            try {
                val jsonObj = JSONObject(jsonStr)
                if (jsonObj.has("content")) {
                    content = jsonObj.optString("content")
                }
                if (jsonObj.has("contentType")) {
                    contentType = jsonObj.optString("contentType")
                }
                if (jsonObj.has("extra")) {
                    extraInfo = jsonObj.optString("extra")
                }
            } catch (var4: JSONException) {
                RLog.e("TextMessage", "JSONException " + var4.message)
            }
        }
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        ParcelUtils.writeToParcel(dest, content)
        ParcelUtils.writeToParcel(dest, contentType)
        ParcelUtils.writeToParcel(dest, extraInfo)
    }

    override fun encode(): ByteArray? {
        val jsonObj = JSONObject()
        try {
            if (!TextUtils.isEmpty(content)) {
                jsonObj.put("content", content)
            }
            if (!TextUtils.isEmpty(contentType)) {
                jsonObj.put("contentType", contentType)
            }
            if (!TextUtils.isEmpty(extraInfo)) {
                jsonObj.put("extra", extraInfo)
            }
        } catch (var4: JSONException) {

        }

        return try {
            jsonObj.toString().toByteArray(charset("UTF-8"))
        } catch (var3: UnsupportedEncodingException) {
            null
        }
    }

    companion object CREATOR : Parcelable.Creator<HyperLinkMessage> {
        override fun createFromParcel(parcel: Parcel): HyperLinkMessage {
            return HyperLinkMessage(parcel)
        }

        override fun newArray(size: Int): Array<HyperLinkMessage?> {
            return arrayOfNulls(size)
        }
    }
}
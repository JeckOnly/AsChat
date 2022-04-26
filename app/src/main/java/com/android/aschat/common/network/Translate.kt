package com.android.aschat.common.network

import com.google.gson.JsonParser
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONArray
import org.json.JSONException
import java.lang.Exception
import java.util.*

object Translate {
    /**
     *	originalText:原始文本
     *  apiKey : 微软翻译key
     *  子线程运行
     */
     fun getTrans(apiKey: String, originalText: String): String {
        try {
            val url = HttpUrl.Builder()
                .scheme("https")
                .host("api.cognitive.microsofttranslator.com")
                .addPathSegment("/translate")
                .addQueryParameter("api-version", "3.0")
                .addQueryParameter("to", getLanguageCode())
                .build()

            // Instantiates the OkHttpClient.
            val client = OkHttpClient()
            val content = "[{\"Text\":\"$originalText\"}]"

            val mediaType = "application/json".toMediaTypeOrNull()
            val body = RequestBody.create(mediaType, content)
            val request = Request.Builder().url(url).post(body)
                .addHeader("Ocp-Apim-Subscription-Key", apiKey)
                .addHeader("Content-type", "application/json")
                .build()
            val response = client.newCall(request).execute()
            val parser = JsonParser()
            val json = parser.parse(response.body?.string())
            return getResult(JSONArray(json.toString()))
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    private fun getLanguageCode(): String {
        var locale = Locale.getDefault().language
        // 国内直翻繁体
        if ("zh" == locale) {
            if ("CN" == Locale.getDefault().country) {

            } else {
                locale = "zh_tw"
            }
        }
        return locale
    }

    private fun getResult(jsonArray: JSONArray): String {
        return try {
            jsonArray.getJSONObject(0).optJSONArray("translations").getJSONObject(0).optString("text")
        } catch (e: JSONException) {
            e.printStackTrace()
            ""
        }
    }
}
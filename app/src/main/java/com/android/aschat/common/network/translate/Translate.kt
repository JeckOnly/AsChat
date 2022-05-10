package com.android.aschat.common.network.translate

import com.android.aschat.common.Constants
import com.android.aschat.common.MyApplication
import com.android.aschat.util.JsonUtil
import com.android.aschat.util.SpConstants
import com.android.aschat.util.SpUtil
import com.google.gson.JsonParser
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONArray
import org.json.JSONException
import java.security.MessageDigest
import java.util.*
import kotlin.collections.HashMap

object Translate {

    private val mApiKey by lazy {
        SpUtil.get(MyApplication.application, SpConstants.Microsoft_Translation_Key, "") as String
    }

    /**
     * 缓存的map
     */
    private lateinit var mTempTranslateMap: TranslateLinkedHashMap

    /**
     * 永久的map
     */
    private lateinit var mForeverTranslateMap: HashMap<String, String>


    /**
     *  note 在子线程运行，模拟器时间跟随网络
     *	originalText:原始文本
     *  apiKey : 微软翻译key
     *  lifecycleScope.launch(Dispatchers.IO) {
     *       val resultContent = async {
     *           Translate.getTrans(translateKey, "the world is beautiful")
     *       }
     *       LogUtil.d("$translateKey    ${resultContent.await()}")
     *   }
     *   @return 报错的话返回原始文本
     */
    fun getTrans(originalText: String): String {
        // 判断有无初始化
        if (!::mTempTranslateMap.isInitialized || !::mForeverTranslateMap.isInitialized) {
            return originalText
        }
        // 获得md5值
        val md5Key = encrypt(originalText)
        // 先在缓存和永久里面找
        if (mTempTranslateMap.containsKey(md5Key)) {
            return mTempTranslateMap[md5Key]!!
        }else if (mForeverTranslateMap.containsKey(md5Key)) {
            return mForeverTranslateMap[md5Key]!!
        }
        // 缓存和永久都没有
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
                .addHeader("Ocp-Apim-Subscription-Key", mApiKey)
                .addHeader("Content-type", "application/json")
                .build()
            val response = client.newCall(request).execute()
            val parser = JsonParser()
            val json = parser.parse(response.body?.string())
            val result = getResult(JSONArray(json.toString()))
            // 写进临时缓存
            mTempTranslateMap[encrypt(result)] = result
            return result
        } catch (e: Exception) {
            e.printStackTrace()
            return originalText
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

    /**
     * 返回一个字符串的md5值
     */
    private fun encrypt(string: String): String {
        var md5: String = ""
        try {
            // 初始化MD5对象
            val instance = MessageDigest.getInstance("MD5")
            // 将字符串变成byte数组
            val bs = string.toByteArray()
            // 得到128位字节数组
            val digest = instance.digest(bs)
            // 转换成16进制
            md5 = bytesToHex(digest)
        } catch (e: Exception) {

        }
        return md5
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val sb = StringBuffer()
        var dig = 0
        for (b in bytes) {
            dig = b.toInt()
            if (dig < 0) {
                dig += 256
            }
            if (dig < 16) {
                sb.append("0")
            }
            sb.append(Integer.toHexString(dig))
        }
        return sb.toString().lowercase(Locale.getDefault())
    }

    fun initTranslate() {
        mTempTranslateMap = TranslateLinkedHashMap()
        if (SpUtil.get(MyApplication.application, SpConstants.Translate_Map, "") as String == "") {
            // 没有缓存，第一次使用应用
            mForeverTranslateMap = HashMap()
        }else {
            // 有缓存
            val tempMapStr = SpUtil.get(MyApplication.application, SpConstants.Translate_Map, "") as String
            mForeverTranslateMap = JsonUtil.json2Any(tempMapStr, HashMap::class.java, String::class.java, String::class.java)
        }
    }

    class TranslateLinkedHashMap(val initSize: Int = Constants.Max_Save_Size): LinkedHashMap<String, String>(Constants.Max_Save_Size) {

        /**
         * 利用它每一次插入数据就会调用的性质
         */
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, String>?): Boolean {
            if (size > Constants.Max_Save_Size) {
                // 缓存落地
                mForeverTranslateMap.putAll(mTempTranslateMap)
                SpUtil.putAndApply(MyApplication.application, SpConstants.Translate_Map, JsonUtil.any2Json(mForeverTranslateMap))
                // 清空lru
                this.clear()
            }
            return false
        }
    }
}
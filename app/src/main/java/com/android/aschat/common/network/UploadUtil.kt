package com.android.aschat.common.network

import android.content.Context
import android.text.TextUtils
import com.android.aschat.feature_home.domain.model.mine.FileBody
import com.android.aschat.feature_login.domain.model.osspolicy.OssPolicy
import com.android.aschat.util.JsonUtil
import com.android.aschat.util.SpConstants
import com.android.aschat.util.SpUtil
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

/**
 * 把图片上传到oss服务器的工具类
 */
object UploadUtil {

    fun getOssHost(context: Context): String{
        return getOssPolicy(context).host
    }

    /**
     * @param filePath 图片的文件路径
     */
    fun getUploadAvatarBody(context: Context, filePath: String): MultipartBody {
        val policy = getOssPolicy(context)
        val body = MultipartBody.Builder().setType(MultipartBody.FORM)
        val fileBody = getFileBody(filePath)
        body.addFormDataPart("ossaccessKeyId", policy.accessKeyId)
        body.addFormDataPart("policy", policy.policy)
        body.addFormDataPart("signature", policy.signature)
        body.addFormDataPart("callback", policy.callback)
        body.addFormDataPart("key", getOssKey(policy.dir, fileBody.fileName))
        body.addFormDataPart(fileBody.fileId, fileBody.fileName, fileBody.fileBody)
        return body.build()
    }

    private fun getOssPolicy(context: Context): OssPolicy {
        val ossPolicyStr = SpUtil.get(
            context,
            SpConstants.Oss_Policy,
            ""
        ) as String
        return JsonUtil.json2Any(ossPolicyStr, OssPolicy::class.java)
    }

    private fun getOssKey(dir: String?, fileName: String?): String {
        if (TextUtils.isEmpty(dir) || TextUtils.isEmpty(fileName)) return ""
        val suffixIndex = fileName?.lastIndexOf(".")
        val fileType = if (suffixIndex != null && suffixIndex > 0) fileName.substring(suffixIndex) else ""
        val ossKey = "${dir}${System.currentTimeMillis()}${fileType}"
        return ossKey
    }

    private fun getFileBody(filePath: String): FileBody {
        val file = File(filePath); //localFile 是用户当前选择的图片
        val body = file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
        return FileBody("file", file.name, body)
    }
}
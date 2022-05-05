package com.android.aschat.common.network.socketIo

import android.os.Message
import com.android.aschat.common.network.ApiUrls
import com.android.aschat.common.network.socketIo.model.*
import com.android.aschat.util.JsonUtil
import com.android.aschat.util.LogUtil
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.lang.Exception
import java.net.URISyntaxException


object SocketIo {

    /**
     * 初始化socket并连接
     *
     * NOTE 重连也走这个方法
     */
    fun initAndConnect(token: String): Socket? {
        val httpClient: OkHttpClient = OkHttpClient.Builder()
            .build()
        val opts = IO.Options().apply {
            query = "token=$token"// getUserToken -> 登录成功后返回的token
            webSocketFactory = httpClient
            callFactory = httpClient
            forceNew = true
            reconnection = true

        }
        var socket: Socket? = null
        try {
        // getSkIoUrl -> 长连url
        // eg(测试环境):
            socket = IO.socket(ApiUrls.BASE_SOCKET_URL, opts)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
        socket?.let {
            // 此处可进行事件注册
            registerEvent(socket)
            // 开始连接
            socket.connect()
        }
        return socket
    }

    /**
     * socket注册监听器
     */
    private fun registerEvent(socket: Socket) {
        socket.on("responseEvent", EmitterListener(socket))
        socket.on("messageEvent", EmitterListener(socket))
        socket.on(Socket.EVENT_CONNECT_TIMEOUT, EmitterListener(socket))
    }

    class EmitterListener(val socket: Socket): Emitter.Listener {
        override fun call(vararg args: Any?) {
            // 切换主线程
            CoroutineScope(Dispatchers.Main).launch {
                handleCall(socket, args)
            }
        }
    }
    
    private fun handleCall(socket: Socket, vararg args: Any?) {
        if (args[0] is JSONObject) {
            LogUtil.d("handleCall " + args[0].toString());
            val jsonObject = args[0] as JsonObject
            if (socket.connected())  return;// 判断Socket io是否断开连接
            try {
                val gson = Gson()
                val code = jsonObject["code"].asString
                if (code == null)  return;
                if (!code.equals("200")) return;
                // 解析数据结构，处理消息数据
                val key = jsonObject["command"].asString
                val keyId = jsonObject["commandId"]
                val jsonObjectData = jsonObject["data"].asJsonObject
                when (key) {
                    "onCall" -> {
                        val onCallModel = gson.fromJson(jsonObjectData, OnCallModel::class.java)
                        LogUtil.d("收到onCall: $onCallModel")
                    }
                    "onHangUp" -> {
                        val onHangUpModel = gson.fromJson(jsonObjectData, OnHangUpModel::class.java)
                        LogUtil.d("收到onHangUp: $onHangUpModel")
                    }
                    "onPickUp" -> {
                        val onPickUpModel = gson.fromJson(jsonObjectData, OnPickUpModel::class.java)
                        LogUtil.d("收到onPickUp: $onPickUpModel")
                    }
                    "estimatedHangUpTime" -> {
                        val estimatedHangUpTimeModel = gson.fromJson(jsonObjectData, EstimatedHangUpTimeModel::class.java)
                        LogUtil.d("收到estimatedHangUpTime: $estimatedHangUpTimeModel")
                    }
                    "availableCoins" -> {
                        val availableCoinsModel = gson.fromJson(jsonObjectData, AvailableCoinsModel::class.java)
                        LogUtil.d("收到availableCoins: $availableCoinsModel")
                    }
                    "onChat" -> {

                    }
                    "messageEvent" -> {
                        val messageEventModel = gson.fromJson(jsonObjectData, MessageEventModel::class.java)
                        LogUtil.d("收到messageEvent: $messageEventModel")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace();
            }
        }
    }
}

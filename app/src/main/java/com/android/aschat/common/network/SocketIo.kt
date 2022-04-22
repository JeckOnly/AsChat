package com.android.aschat.common.network

import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import okhttp3.OkHttpClient
import java.net.URISyntaxException


object SocketIo {
    fun initAndConnect(token: String) {
        val httpClient: OkHttpClient = OkHttpClient.Builder()
            .build()
        val opts = IO.Options()
        opts.forceNew = true
        // getUserToken -> 登录成功后返回的token
        opts.query = "token=$token"
        opts.webSocketFactory = httpClient
        opts.callFactory = httpClient
        var mSkIo: Socket? = null
        try {
        // getSkIoUrl -> 长连url
        // eg(测试环境):
            mSkIo = IO.socket(ApiUrls.BASE_SOCKET_URL, opts)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
        mSkIo?.let {
            // 此处可进行事件注册
            registerEvent(mSkIo)
            // 开始连接
            mSkIo.connect()
        }
    }

    private fun registerEvent(mSkIo: Socket) {
        mSkIo.on("responseEvent", object : Emitter.Listener {
            override fun call(vararg args: Any?) {
                TODO("Not yet implemented")
            }
        })
        mSkIo.on("messageEvent", object : Emitter.Listener {
            override fun call(vararg args: Any?) {
                TODO("Not yet implemented")
            }
        })
        mSkIo.on(Socket.EVENT_CONNECT_TIMEOUT,object : Emitter.Listener {
            override fun call(vararg args: Any?) {
                TODO("Not yet implemented")
            }
        })
    }
}
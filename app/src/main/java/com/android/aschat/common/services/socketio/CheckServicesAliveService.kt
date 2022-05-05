package com.android.aschat.common.services.socketio

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.android.aschat.common.network.socketIo.SocketIo
import com.android.aschat.util.DialogUtil
import com.android.aschat.util.LogUtil
import com.android.aschat.util.SpConstants
import com.android.aschat.util.SpUtil
import io.socket.client.Socket
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class CheckServicesAliveService : Service() {

    private lateinit var mScheduleFuture: ScheduledFuture<*>

    override fun onCreate() {
        super.onCreate()
        // NOTE 初始化时一直尝试连接，如果socket一直为Null就卡在这，成功再出来
        val token = SpUtil.get(applicationContext, SpConstants.TOKEN, "") as String
        val createSuccess = recreateAction(applicationContext, token)
        if (createSuccess) {
            LogUtil.d("socket创建成功")
        }else {
            LogUtil.d("socket创建失败")
        }
        // NOTE 1)心跳定时器
        mScheduleFuture = Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(
            {
                // 需要socket创建成功才做下一步
                if (mSocket == null) {
                    val recreateSuccess = recreateAction(applicationContext, token)
                    if (recreateSuccess) {
                        LogUtil.d("socket重建成功")
                    }else {
                        // NOTE 如果没有创建成功，就不用检查是否有连接了，没意义
                        LogUtil.d("socket重建失败")
                        return@scheduleWithFixedDelay
                    }
                }
                // 检查socket是否断开连接 NOTE 客户端请自行设计定时器逻辑，每一分钟校验当前socket 的连接状态。如果处于断开状态，请进行重连机制。
                if (!checkSocketAlive()) {
                    // 重连机制
                    LogUtil.d("socket断开连接")
                    LogUtil.d("开始重连")
                    reconnectAction(applicationContext)
                }else {
                    LogUtil.d("socket保持连接")
                }
            },
            ONE_MINUTE,
            ONE_MINUTE,
            TimeUnit.MILLISECONDS
        )
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mScheduleFuture.isInitialized && !mScheduleFuture.isCancelled) {
            mScheduleFuture.cancel(true)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
       return null
    }

    companion object {
        private var mSocket: Socket? = null

        private const val ONE_MINUTE = 60 * 1000L
        private const val ONE_SECOND = 1000L
        private const val mReconnectCountMax = 5
        private const val mRecreatedCountMax = 10
        private var mReconnectCount = 0
        private var mRecreatedCount = 0

        /**
         * 检查socket是否存活
         */
        fun checkSocketAlive(): Boolean {
            LogUtil.d("检查socket是否存活")
            return mSocket!!.connected()
        }

        /**
         * 建立socket
         */
        private fun recreateAction(context: Context, token: String): Boolean {
            while (true) {
                val socket: Socket? = SocketIo.initAndConnect(token)
                if (socket != null) {
                    mSocket = socket
                    return true
                }
                mRecreatedCount++
                if (mRecreatedCount == mRecreatedCountMax) {
                    // 全局弹窗 网络有问题
                    val dialog = DialogUtil.createWrongNetworkDialog(context) { dialog ->
                        dialog.dismiss()
                    }
                    dialog.show()
                    mRecreatedCount = 0
                    return false
                }
            }
        }

        /**
         * 重连机制
         *
         * NOTE 重连间隔一秒，最多五次。如五次都没连接成功，请全局弹窗告知用户当前网络状态异常，请检查当前网络情况。
         */
        fun reconnectAction(context: Context) {
            while (true) {
                mSocket!!.connect()
                if (!mSocket!!.connected()) {
                    // 没有连接成功
                    mReconnectCount++
                    if (mReconnectCount == mReconnectCountMax) {
                        // 全局弹窗 网络有问题
                        val dialog = DialogUtil.createWrongNetworkDialog(context) { dialog ->
                            dialog.dismiss()
                        }
                        dialog.show()
                        mReconnectCount = 0
                        return
                    }
                    Thread.sleep(ONE_SECOND)
                }else {
                    // 连接成功
                    LogUtil.d("重连成功")
                    return
                }
            }
        }

        fun getSocket(): Socket?{
            return mSocket
        }
    }
}

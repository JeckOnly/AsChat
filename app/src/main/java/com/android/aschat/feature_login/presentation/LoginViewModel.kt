package com.android.aschat.feature_login.presentation

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.aschat.R
import com.android.aschat.common.Constants
import com.android.aschat.common.MyApplication
import com.android.aschat.common.network.translate.Translate
import com.android.aschat.common.services.socketio.CheckServicesAliveService
import com.android.aschat.feature_home.presentation.HomeActivity
import com.android.aschat.feature_host.presentation.HostActivity
import com.android.aschat.feature_login.domain.model.appconfig.ConfigItemStrStr
import com.android.aschat.feature_login.domain.model.coin.CoinGoodPromotion
import com.android.aschat.feature_login.domain.model.coin.GetCoinGood
import com.android.aschat.feature_login.domain.model.osspolicy.OssPolicy
import com.android.aschat.feature_login.domain.repo.LoginRepo
import com.android.aschat.feature_rongyun.MyConversationActivity
import com.android.aschat.feature_rongyun.rongyun.custom_message_kind.HyperLinkMessage
import com.android.aschat.feature_rongyun.rongyun.ui.MyHyperLinkMessageProvider
import com.android.aschat.feature_rongyun.rongyun.ui.MyTextMessageProvider
import com.android.aschat.feature_rongyun.rongyun.ui.MyUserInfoProvider
import com.android.aschat.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rong.imkit.RongIM
import io.rong.imkit.config.ConversationListBehaviorListener
import io.rong.imkit.config.RongConfigCenter
import io.rong.imkit.conversation.messgelist.provider.TextMessageItemProvider
import io.rong.imkit.conversationlist.model.BaseUiConversation
import io.rong.imkit.utils.RouteUtils
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject
import javax.inject.Named


@HiltViewModel
class LoginViewModel @Inject constructor(
    @Named("LoginRepo") private val repo: LoginRepo
): ViewModel() {

    fun onEvent(event: LoginEvents) {
        when (event) {
            is LoginEvents.LoginEvent -> {
                viewModelScope.launch {
                    // 登录
                    val oldUserId = SpUtil.get(event.context, SpConstants.USERID, "")
                    val androidId = AppUtil.getAndroidId(event.context)

                    // 1) 登录
                    val loginResponse = repo.login(Constants.OauthType, androidId)
                    if (loginResponse.code == 0) {
                        // 成功
                        if (loginResponse.data!!.isFirstRegister) {
                            // 第一次登录，将信息写入数据库
                            SpUtil.putAndApply(event.context, SpConstants.USERINFO, JsonUtil.any2Json(loginResponse.data.userInfo))
                        }else {
                            // 不是第一次登录，更新用户信息(目前逻辑是覆盖)
                            SpUtil.putAndApply(event.context, SpConstants.USERINFO, JsonUtil.any2Json(loginResponse.data.userInfo))
                        }

                        if (oldUserId != loginResponse.data.userInfo.userId) {
                            // 更新user id
                            SpUtil.putAndApply(event.context, SpConstants.USERID, loginResponse.data.userInfo.userId)
                        }
                        val token = loginResponse.data.token
                        // 保存token
                        SpUtil.putAndApply(event.context, SpConstants.TOKEN, token)

                        supervisorScope {
                            // 2) 获取策略
                            val jobStrategy = launch {
                                val strategyResponse = repo.getStrategy()
                                if (strategyResponse.code == 0) {
                                    // 成功
                                    // 保存策略
                                    SpUtil.putAndApply(event.context, SpConstants.STRATEGY, JsonUtil.any2Json(strategyResponse.data!!))
                                }
                            }

                            // 3) 获取金币促销商品
                            val jobCoinGoodPromotion = launch {
                                val response = repo.getCoinGoodsPromotion(GetCoinGood(true, Constants.PayChannel))
                                if (response.code == 0) {
                                    // 成功
                                    // 保存金币商品促销信息
                                    val data = response.data
                                    if (data != null) {
                                        SpUtil.putAndApply(event.context, SpConstants.COIN_GOODS_PROMOTION, JsonUtil.any2Json(data))
                                    }else {
                                        // 如果data为null，设置一个非法的默认值
                                        SpUtil.putAndApply(event.context, SpConstants.COIN_GOODS_PROMOTION, JsonUtil.any2Json(CoinGoodPromotion()))
                                    }
                                    // 保存拉取倒计时的时间戳
                                    SpUtil.putAndApply(event.context, SpConstants.COIN_GOODS_PROMOTION_TEMP_STAMP, System.currentTimeMillis())
                                }
                            }

                            jobCoinGoodPromotion.join()

                            // 4) 获取金币商品 在促销接口调用之后才调用这个接口
                            val jobCoinGoods = launch {
                                val coinGoodResponse = repo.getCoinGoods(GetCoinGood(true, Constants.PayChannel))
                                if (coinGoodResponse.code == 0) {
                                    // 成功
                                    // 保存金币商品信息
                                    SpUtil.putAndApply(event.context, SpConstants.COIN_GOODS, JsonUtil.any2Json(coinGoodResponse.data!!))
                                }
                            }

                            // 5) 获取app config
                            val jobAppConfig = launch {
                                val configResponse = repo.getAppConfig(SpUtil.get(event.context, SpConstants.CONFIG_VER, 0) as Int)
                                if (configResponse.code == 0) {
                                    // 成功
                                    // 保存配置
                                    // NOTE 后台如果有新数据就会进去
                                    if (configResponse.data != null) {
                                        // 返回的list是空，就不写入，否则覆盖写入
                                        val list = configResponse.data.items
                                        // NOTE 如果上面那个if进来了这个if一般都会进去
                                        if (list.isNotEmpty()) {
                                            // 存储配置版本号
                                            SpUtil.putAndApply(
                                                event.context,
                                                SpConstants.CONFIG_VER,
                                                configResponse.data.ver.toInt()
                                            )
                                            // 存储总的config
                                            SpUtil.putAndApply(
                                                event.context,
                                                SpConstants.CONFIG_List,
                                                JsonUtil.any2Json(list)
                                            )
                                            // 存储微软翻译key
                                            val microKey = list.first { configItemBase ->
                                                configItemBase.name == Constants.MicroTransName
                                            } as ConfigItemStrStr
                                            SpUtil.putAndApply(
                                                event.context,
                                                SpConstants.Microsoft_Translation_Key,
                                                microKey.data
                                            )
                                            // 存储融云key
                                            val rckKey = list.first { configItemBase ->
                                                configItemBase.name == Constants.RckName
                                            } as ConfigItemStrStr
                                            SpUtil.putAndApply(
                                                event.context,
                                                SpConstants.Rck_Key,
                                                rckKey.data
                                            )
                                            // 存储声网key
                                            val rtckKey = list.first { configItemBase ->
                                                configItemBase.name == Constants.RtckName
                                            } as ConfigItemStrStr
                                            SpUtil.putAndApply(
                                                event.context,
                                                SpConstants.Rtck_Key,
                                                rtckKey.data
                                            )
                                        }
                                    }
                                }
                                initRongyun(event.context, loginResponse.data.userInfo.rongcloudToken)
                            }

//                            6) 获取oss服务器相关
                            val jobOss = launch {
                                val ossResponse = repo.getOssPolicy()
                                if (ossResponse.code == 0) {
                                    SpUtil.putAndApply(
                                        event.context,
                                        SpConstants.Oss_Policy,
                                        JsonUtil.any2Json(ossResponse.data!!)
                                    )
                                }else {
                                    SpUtil.putAndApply(
                                        event.context,
                                        SpConstants.Oss_Policy,
                                        JsonUtil.any2Json(OssPolicy())
                                    )
                                }
                            }
                            jobStrategy.join()
                            jobCoinGoods.join()
                            jobAppConfig.join()
                            jobOss.join()

                            // NOTE 翻译模块初始化
                            Translate.initTranslate()

                            // NOTE 启动后台的长链检查服务
                            event.context.startService(Intent(event.context, CheckServicesAliveService::class.java))

                            // end)跳转
                            if (loginResponse.data.isFirstRegister) {
                                // 第一次登录
                                event.navController.navigate(R.id.action_splashFragment_to_fastLoginFragment)
                            }else {
                                // 不是第一次登录
                                //                            event.navController.navigate(R.id.action_splashFragment_to_homeActivity)
                                val intent = Intent(event.context, HomeActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                event.context.startActivity(intent)
                            }
                        }

                    }
                }
            }
            is LoginEvents.FastLogin2HomeActivity -> {
//                event.navController.navigate(R.id.action_fastLoginFragment_to_homeActivity)
                val intent = Intent(event.context, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                event.context.startActivity(intent)
            }
        }
    }

    private fun initRongyun(context: Context, token: String) {
        // NOTE 初始化融云
        try {
            val rckKey = SpUtil.get(context, SpConstants.Rck_Key, "") as String
            RongIM.init(MyApplication.application, rckKey)
            LogUtil.d("融云初始化胜利")
        }catch (e: Exception) {
            LogUtil.d("融云初始化失败   ${e.stackTrace}")
        }

        // NOTE 设置融云如何获取用户头像等信息
        RongIM.setUserInfoProvider(
            MyUserInfoProvider
            {
                repo.getHostInfo(it)
            },
            true
        )

        // NOTE 设置融云会话列表的回调监听
        RongIM.setConversationListBehaviorListener(object : ConversationListBehaviorListener {
            // 点击头像回调
            override fun onConversationPortraitClick(
                context: Context,
                p1: Conversation.ConversationType?,
                userId: String?
            ): Boolean {
                LogUtil.d("融云会话列表点击头像 userId $userId")
                val intent = Intent(context, HostActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    putExtra(Constants.WhereFrom, Constants.FromRongyunList)
                    putExtra("userId", userId)
                }
                context.startActivity(intent)
                return true
            }

            override fun onConversationPortraitLongClick(
                p0: Context?,
                p1: Conversation.ConversationType?,
                p2: String?
            ): Boolean {
                return false
            }

            override fun onConversationLongClick(
                p0: Context?,
                p1: View?,
                p2: BaseUiConversation?
            ): Boolean {
                return false
            }

            override fun onConversationClick(
                p0: Context?,
                p1: View?,
                p2: BaseUiConversation?
            ): Boolean {
                return false
            }
        })

//        // NOTE 注册自定义会话列表
//        RouteUtils.registerActivity(RouteUtils.RongActivityType.ConversationListActivity, HomeActivity::class.java)
        // NOTE 注册自定义会话界面
        RouteUtils.registerActivity(RouteUtils.RongActivityType.ConversationActivity, MyConversationActivity::class.java)
        // NOTE 注册自定义文本消息UI
        RongConfigCenter.conversationConfig().replaceMessageProvider(TextMessageItemProvider::class.java, MyTextMessageProvider())
        // NOTE 注册自定义消息
        RongIMClient.registerMessageType(listOf(HyperLinkMessage::class.java))
        RongConfigCenter.conversationConfig().addMessageProvider(MyHyperLinkMessageProvider())

        // NOTE 连接融云
        try {
            // SDK 本身有重连机制，在一个应用生命周期内不须多次调用connect() 。否则可能触发多个回调，触发导致回调被清除。
            RongIM.connect(token, object : RongIMClient.ConnectCallback() {
                override fun onSuccess(p0: String?) {
                    LogUtil.d("融云连接成功回调   $p0")
                    // TODO: 设置长链断开的消息监听
                }

                override fun onError(p0: RongIMClient.ConnectionErrorCode?) {
                    // 连接失败并返回对应的连接错误码，开发者需要参考连接相关错误码进行不同业务处理。
                    LogUtil.d("融云连接失败回调   ${p0.toString()}")
                }

                override fun onDatabaseOpened(p0: RongIMClient.DatabaseOpenStatus?) {
                    // 本地数据库打开状态回调。当回调 DATABASE_OPEN_SUCCESS 时，说明本地数据库打开，此时可以拉取本地历史会话及消息，适用于离线登录场景。
                    LogUtil.d("融云onDatabaseopened回调   ${p0.toString()}")
                }

            })
            LogUtil.d("融云连接胜利")
        }catch (e: Exception) {
            LogUtil.d("融云连接失败   ${e.stackTrace}")
        }
    }
}

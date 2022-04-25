package com.android.aschat.feature_login.presentation

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.aschat.R
import com.android.aschat.common.Constants
import com.android.aschat.feature_home.presentation.HomeActivity
import com.android.aschat.feature_login.domain.model.coin.CoinGoodPromotion
import com.android.aschat.feature_login.domain.model.coin.GetCoinGood
import com.android.aschat.feature_login.domain.repo.LoginRepo
import com.android.aschat.util.AppUtil
import com.android.aschat.util.JsonUtil
import com.android.aschat.util.SpConstants
import com.android.aschat.util.SpUtil
import dagger.hilt.android.lifecycle.HiltViewModel
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

                            // 3) 获取金币商品
                            val jobCoinGoods = launch {
                                val coinGoodResponse = repo.getCoinGoods(GetCoinGood(true, Constants.PayChannel))
                                if (coinGoodResponse.code == 0) {
                                    // 成功
                                    // 保存金币商品信息
                                    SpUtil.putAndApply(event.context, SpConstants.COIN_GOODS, JsonUtil.any2Json(coinGoodResponse.data!!))
                                }
                            }

                            // 4) 获取金币促销商品
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

                            jobStrategy.join()
                            jobCoinGoods.join()
                            jobCoinGoodPromotion.join()

                            // end)跳转
                            if (loginResponse.data!!.isFirstRegister) {
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
}
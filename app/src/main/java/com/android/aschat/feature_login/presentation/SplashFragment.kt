package com.android.aschat.feature_login.presentation

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.android.aschat.R
import com.android.aschat.util.FontUtil
import dagger.hilt.android.AndroidEntryPoint


/**
 * 1）登录
 * 2）下拉网络配置
 */
@AndroidEntryPoint
class SplashFragment: Fragment(R.layout.login_splash_fragment) {

    private val mAppName: TextView by lazy { requireView().findViewById(R.id.splash_name) }

    private val mViewModel: LoginViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAppName.typeface = FontUtil.getTypeface(requireContext())
        mViewModel.onEvent(LoginEvents.LoginEvent(requireContext(), findNavController()))
    }
}
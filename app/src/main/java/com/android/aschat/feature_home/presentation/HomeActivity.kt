package com.android.aschat.feature_home.presentation

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.android.aschat.R
import com.android.aschat.common.BaseActivity
import com.android.aschat.util.LogUtil
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeActivity: BaseActivity() {
    private val mBottomNavView: BottomNavigationView by lazy {
        findViewById(R.id.home_bottom_nav)
    }
    override fun provideLayoutId(): Int {
        return R.layout.home_acty
    }

    override fun init() {
        super.init()

    }

    override fun onStart() {
        super.onStart()
        mBottomNavView.setupWithNavController(findNavController(R.id.home_nav_host_fragment))
        findNavController(R.id.home_nav_host_fragment).addOnDestinationChangedListener { controller: NavController, destination: NavDestination, arguments: Bundle? ->
            if(destination.id == R.id.wallFragment || destination.id == R.id.messageFragment || destination.id == R.id.userFragment) {
                mBottomNavView.visibility = View.VISIBLE
            } else {
                LogUtil.d("hide fragment ${destination.id}")
                mBottomNavView.visibility = View.GONE
            }
        }
        mBottomNavView.setOnItemSelectedListener { item: MenuItem ->
            // bottom UI不返回第一个
            val options = NavOptions.Builder()
                //从放回栈中移除指定目的地
                .setPopUpTo(findNavController(R.id.home_nav_host_fragment).currentDestination!!.id, true)
                .setLaunchSingleTop(true)
                .build()
            try {
                findNavController(R.id.home_nav_host_fragment).navigate(item.itemId, null, options)
                true
            } catch (e: IllegalArgumentException) {
                false
            }
        }
    }
}
package com.android.aschat.common

import android.app.Application
import android.content.Context
import com.android.aschat.R
import com.drake.brv.BR
import com.drake.brv.utils.BRV
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import dagger.hilt.android.HiltAndroidApp

/**
 * 使用hilt需要加这个注释
 */
@HiltAndroidApp
class MyApplication :Application(){
    override fun onCreate() {
        super.onCreate()

        ClassicsFooter.REFRESH_FOOTER_PULLING = getString(R.string.footer_pulling);//"上拉加载更多";
        ClassicsFooter.REFRESH_FOOTER_RELEASE = getString(R.string.footer_release);//"释放立即加载";
        ClassicsFooter.REFRESH_FOOTER_REFRESHING = getString(R.string.footer_refreshing);//"正在刷新...";
        ClassicsFooter.REFRESH_FOOTER_LOADING = getString(R.string.footer_loading);//"正在加载...";
        ClassicsFooter.REFRESH_FOOTER_FINISH = getString(R.string.footer_finish);//"加载完成";
        ClassicsFooter.REFRESH_FOOTER_FAILED = getString(R.string.footer_failed);//"加载失败";
        ClassicsFooter.REFRESH_FOOTER_NOTHING = getString(R.string.footer_nothing);//"没有更多数据了";

        // 初始化主播墙的下拉刷新
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout -> MaterialHeader(this) }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout -> ClassicsFooter(this) }

        BRV.modelId = BR.model
    }
}
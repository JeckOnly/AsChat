package com.android.aschat.feature_host.presentation.hostinfo.hostdetail

import android.content.Context
import com.android.aschat.R
import razerdp.basepopup.BasePopupWindow


class ReportPopUp(val context: Context): BasePopupWindow(context) {
    init {
        setContentView(R.layout.host_detail_popupwindow)
    }
}
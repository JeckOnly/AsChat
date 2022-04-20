package com.android.aschat.feature_host.presentation.hostinfo.FillScreenImage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import coil.load
import com.android.aschat.R

/**
 * 作为展示一个图片的fragment 全屏查看
 */
class FullScreenOneImageFragment(private val imageUrl: String) : Fragment(R.layout.host_fillscreen_showoneimage_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ImageView>(R.id.host_fillscreenimage_picture).load(imageUrl)
    }
}
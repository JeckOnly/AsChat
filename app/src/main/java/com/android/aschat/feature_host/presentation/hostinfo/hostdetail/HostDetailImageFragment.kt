package com.android.aschat.feature_host.presentation.hostinfo.hostdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.android.aschat.R
import com.android.aschat.feature_host.presentation.HostEvents
import com.android.aschat.feature_host.presentation.HostViewModel

/**
 * 主播界面展示图片的fragment，不是全屏查看那个
 */
class HostDetailImageFragment(private val imageUrl: String, private val position: Int) : Fragment(R.layout.host_detail_image_fragment) {

    private val mViewModel: HostViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ImageView>(R.id.host_detail_picture).apply {
            load(imageUrl)
            setOnClickListener {
                mViewModel.onEvent(HostEvents.JumpFullScreenImage(findNavController(), position))
            }
        }
    }
}
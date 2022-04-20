package com.android.aschat.feature_host.presentation.hostinfo.FillScreenImage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.aschat.databinding.HostFillscreenFragmentBinding
import com.android.aschat.feature_host.presentation.HostEvents
import com.android.aschat.feature_host.presentation.HostViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * 全屏查看图片的fragment
 */
@AndroidEntryPoint
class FullScreenImageFragment: Fragment() {

    private lateinit var mBinding: HostFillscreenFragmentBinding
    private val mViewModel: HostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = HostFillscreenFragmentBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        initWidget()
    }

    private fun initBinding() {

    }

    private fun initWidget() {
        // 设置viewpager及其指示器
        mViewModel.pictureUrls.observe(viewLifecycleOwner) {
            // 初始化图片view page
            mBinding.hostFillscreenimagePager.apply {
                adapter = ShowOneImageAdapter(this@FullScreenImageFragment)
                setCurrentItem(mViewModel.picturePositionClicked, false)
            }
            // 设置indicator指示器
            mBinding.hostFillscreenimageIndicator.apply {
                setupWithViewPager(mBinding.hostFillscreenimagePager)
                setInitPage(mViewModel.picturePositionClicked)
            }
        }
        mBinding.hostFillscreenimageBack.setOnClickListener {
            mViewModel.onEvent(HostEvents.ExitFullScreenImage(findNavController()))
        }
    }

    inner class ShowOneImageAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int {
            return mViewModel.pictureUrls.value?.size ?: 0
        }

        override fun createFragment(position: Int): Fragment {
            return FullScreenOneImageFragment(mViewModel.pictureUrls.value?.get(position) ?: "")
        }
    }
}
package com.android.aschat.feature_host.presentation.hostinfo.shortvideoshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.aschat.R
import com.android.aschat.databinding.HostShowshortvideoFragmentBinding
import com.android.aschat.feature_home.domain.model.wall.subtag.HostData
import com.android.aschat.feature_host.presentation.HostEvents
import com.android.aschat.feature_host.presentation.HostViewModel
import com.android.aschat.util.FontUtil
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle


/**
 * 短视频页
 */
class ShowShortVideoFragment: Fragment() {

    private lateinit var mBinding: HostShowshortvideoFragmentBinding
    private val mViewModel: HostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = HostShowshortvideoFragmentBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        initWidget()
    }

    private fun initBinding() {
        mBinding.viewmodel = mViewModel
    }

    private fun initWidget() {
        mViewModel.videoUrls.observe(viewLifecycleOwner) {
            // 设置viewpager及其指示器
            mBinding.videoPager.apply {
                adapter = ShowOneVideoAdapter(this@ShowShortVideoFragment)
                setCurrentItem(mViewModel.videoPositionClicked, false)
            }

            mBinding.videoIndicator.apply {
                setSliderColor(R.color.white, R.color.white)
                setSliderWidth(10f)
                setSliderHeight(5f)
                setSlideMode(IndicatorSlideMode.WORM)
                setIndicatorStyle(IndicatorStyle.DASH)
                setupWithViewPager(mBinding.videoPager)
            }
        }
        mBinding.videoCall.typeface = FontUtil.getTypeface(requireContext())
        mBinding.videoBack.setOnClickListener {
            mViewModel.onEvent(HostEvents.ExitFullScreenVideo(findNavController()))
        }
    }

    inner class ShowOneVideoAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int {
            return mViewModel.videoUrls.value?.size ?: 0
        }

        override fun createFragment(position: Int): Fragment {
            return ShowOneVideoFragment(mViewModel.videoUrls.value?.get(position) ?: "")
        }
    }
}
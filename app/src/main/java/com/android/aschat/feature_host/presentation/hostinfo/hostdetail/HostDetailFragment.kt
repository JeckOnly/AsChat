package com.android.aschat.feature_host.presentation.hostinfo.hostdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.aschat.R
import com.android.aschat.databinding.HostDetailFragmentBinding
import com.android.aschat.feature_host.domain.rv.HostDetailVideoRvAdapter
import com.android.aschat.feature_host.presentation.HostActivity
import com.android.aschat.feature_host.presentation.HostEvents
import com.android.aschat.feature_host.presentation.HostViewModel
import com.android.aschat.util.FontUtil
import com.android.aschat.util.LogUtil
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle
import razerdp.basepopup.BasePopupWindow

class HostDetailFragment : Fragment() {

    private lateinit var mBinding: HostDetailFragmentBinding
    private val mViewModel: HostViewModel by activityViewModels()
    private val mPopupWindow: BasePopupWindow by lazy {
        ReportPopUp(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = HostDetailFragmentBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        initWidget()
    }

    private fun initBinding() {
        LogUtil.d("$mBinding")
        mBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = mViewModel
            videoAdapter = HostDetailVideoRvAdapter { position ->
                mViewModel.onEvent(HostEvents.JumpFullScreenVideo(findNavController(), position))
            }
        }

        mViewModel.pictureUrls.observe(viewLifecycleOwner) {
            // 初始化图片view page
            mBinding.hostDetailPager.adapter = HostDetailImageAdapter(this)
            // 设置indicator指示器
            mBinding.hostDetailIndicator.apply {
                setSliderColor(R.color.white, R.color.white)
                setSliderWidth(10f)
                setSliderHeight(10f)
                setSlideMode(IndicatorSlideMode.WORM)
                setIndicatorStyle(IndicatorStyle.CIRCLE)
                setupWithViewPager(mBinding.hostDetailPager)
            }
        }
        mViewModel.videoThumbImages.observe(viewLifecycleOwner) {
            mBinding.videoAdapter?.submitList(it.toMutableList())
        }
    }

    private fun initWidget() {
        mBinding.hostDetailBack.setOnClickListener {
            mViewModel.onEvent(HostEvents.ExitHostDetail(requireActivity() as HostActivity))
        }
        mBinding.hostDetailMore.setOnClickListener {
           mPopupWindow.showPopupWindow(mBinding.hostDetailMore)
        }
        mBinding.hostDetailVideoCallBar.typeface = FontUtil.getTypeface(requireContext())
    }

    inner class HostDetailImageAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int {
            return mViewModel.pictureUrls.value?.size ?: 0
        }

        override fun createFragment(position: Int): Fragment {
            return HostDetailImageFragment(mViewModel.pictureUrls.value?.get(position) ?: "", position)
        }
    }
}
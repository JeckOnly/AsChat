package com.android.aschat.feature_host.presentation.hostinfo.hostdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.aschat.R
import com.android.aschat.databinding.HostDetailFragmentBinding
import com.android.aschat.feature_home.domain.model.wall.subtag.HostData
import com.android.aschat.feature_home.presentation.HomeEvents
import com.android.aschat.feature_home.presentation.wall.category.MainSpaceDecoration
import com.android.aschat.feature_host.domain.model.hostdetail.extrainfo.GiftAndNumber
import com.android.aschat.feature_host.domain.rv.HostDetailVideoRvAdapter
import com.android.aschat.feature_host.presentation.HostActivity
import com.android.aschat.feature_host.presentation.HostEvents
import com.android.aschat.feature_host.presentation.HostViewModel
import com.android.aschat.util.FontUtil
import com.android.aschat.util.LogUtil
import com.android.aschat.util.setHostStatus
import com.drake.brv.listener.ItemDifferCallback
import com.drake.brv.utils.grid
import com.drake.brv.utils.setup
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
        mBinding.apply {
            hostDetailBack.setOnClickListener {
                mViewModel.onEvent(HostEvents.ExitHostDetail(requireActivity() as HostActivity))
            }
            hostDetailMore.setOnClickListener {
                mPopupWindow.showPopupWindow(mBinding.hostDetailMore)
            }
            hostDetailVideoCallBar.typeface = FontUtil.getTypeface(requireContext())
            hostDetailFollow.setOnClickListener {
                mViewModel.onEvent(HostEvents.ClickFollow)
            }
        }
        // 礼物列表设置
        val giftRvAdapter = mBinding.hostDetailGiftRv.grid(
            spanCount = 4
        ).apply {
            this.addItemDecoration(
                MainSpaceDecoration(requireContext(), 6)
            )
        }.setup {
            addType<GiftAndNumber>(R.layout.host_detail_gift_item)
        }
        // 观察状态变化
        mViewModel.status.observe(viewLifecycleOwner) {
            setHostStatus(mBinding.hostDetailStatus, it)
            setVideoCallStatus(mBinding.hostDetailVideoCallBar, it)
        }
        // 观察是否follow
        mViewModel.follow.observe(viewLifecycleOwner) {
            setFollowRedGray(mBinding.hostDetailFollow, it)
        }
        // 观察礼物列表
        mViewModel.giftMap.observe(viewLifecycleOwner) {
            val giftAndNumberList = mutableListOf<GiftAndNumber>()
            for (gift in it.keys) {
                giftAndNumberList.add(GiftAndNumber(gift, it[gift] ?:"0"))
            }
            giftRvAdapter.models = giftAndNumberList
        }
    }

    inner class HostDetailImageAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int {
            return mViewModel.pictureUrls.value?.size ?: 0
        }

        override fun createFragment(position: Int): Fragment {
            return HostDetailImageFragment(mViewModel.pictureUrls.value?.get(position) ?: "", position)
        }
    }

    private fun setFollowRedGray(imageView: ImageView, isFollow: Boolean) {
        if (isFollow) {
            imageView.setImageResource(R.mipmap.ic_love_gray)
        }else {
            imageView.setImageResource(R.mipmap.ic_love)
        }
    }
    private fun setVideoCallStatus(button: Button, status: String) {
        if (status == null) {
            button.setBackgroundResource(R.drawable.shape_button_gray_white)
            button.isClickable = false
            return
        }
        when (status) {
            "Online" -> {
                // 只有在线状态可以点击
                button.setBackgroundResource(R.drawable.shape_button_red_purple)
                button.isClickable = true
            }
            "Busy" -> {
                button.setBackgroundResource(R.drawable.shape_button_gray_white)
                button.isClickable = false
            }
            "Incall" -> {
                button.setBackgroundResource(R.drawable.shape_button_gray_white)
                button.isClickable = false
            }
            "Offline" -> {
                button.setBackgroundResource(R.drawable.shape_button_gray_white)
                button.isClickable = false
            }
            "Available" -> {
                button.setBackgroundResource(R.drawable.shape_button_gray_white)
                button.isClickable = false
            }
            else -> {
                button.setBackgroundResource(R.drawable.shape_button_gray_white)
                button.isClickable = false
            }
        }
    }
}
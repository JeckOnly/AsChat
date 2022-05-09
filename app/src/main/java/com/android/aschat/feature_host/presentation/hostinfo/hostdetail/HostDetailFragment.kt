package com.android.aschat.feature_host.presentation.hostinfo.hostdetail

import android.app.Dialog
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
import com.android.aschat.common.Constants
import com.android.aschat.databinding.HostDetailFragmentBinding
import com.android.aschat.feature_home.domain.model.wall.subtag.HostData
import com.android.aschat.feature_home.presentation.HomeEvents
import com.android.aschat.feature_home.presentation.wall.category.MainSpaceDecoration
import com.android.aschat.feature_host.domain.model.hostdetail.extrainfo.GiftAndNumber
import com.android.aschat.feature_host.domain.rv.HostDetailVideoRvAdapter
import com.android.aschat.feature_host.presentation.HostActivity
import com.android.aschat.feature_host.presentation.HostEvents
import com.android.aschat.feature_host.presentation.HostViewModel
import com.android.aschat.util.DialogUtil
import com.android.aschat.util.FontUtil
import com.android.aschat.util.LogUtil
import com.android.aschat.util.setHostStatus
import com.drake.brv.listener.ItemDifferCallback
import com.drake.brv.utils.grid
import com.drake.brv.utils.setup
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle
import io.rong.imkit.utils.RouteUtils
import io.rong.imlib.model.Conversation
import razerdp.basepopup.BasePopupWindow

class HostDetailFragment : Fragment() {

    private lateinit var mBinding: HostDetailFragmentBinding
    private val mViewModel: HostViewModel by activityViewModels()
    private val mPopupWindow: BasePopupWindow by lazy {
        ReportPopUp(requireContext())
    }
    private lateinit var mFirstBlockDialog: Dialog
    private lateinit var mSecondBlockDialog: Dialog

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
//                mPopupWindow.showPopupWindow(mBinding.hostDetailMore)
                mSecondBlockDialog = DialogUtil.createSecondBlockDialog(
                    requireContext(),
                    mViewModel.blocked.value?:false,
                    {
                        mViewModel.onEvent(HostEvents.ClickReport(Constants.Politiclsensitive))
                        it.dismiss()
                    },
                    {
                        mViewModel.onEvent(HostEvents.ClickReport(Constants.Falsegender))
                        it.dismiss()
                    },
                    {
                        mViewModel.onEvent(HostEvents.ClickReport(Constants.Fraud))
                        it.dismiss()
                    },
                    {
                        mViewModel.onEvent(HostEvents.ClickBlock)
                        it.dismiss()
                    },
                    {
                        mViewModel.onEvent(HostEvents.ClickReport())
                        it.dismiss()
                    },
                    {
                        it.dismiss()
                    },
                )
                mFirstBlockDialog = DialogUtil.createFirstBlockDialog(
                    requireContext(),
                    mViewModel.follow.value?:false,// 一般来说不为null
                    mViewModel.blocked.value?:false,
                    {
                        // 点击关注
                        mViewModel.onEvent(HostEvents.ClickFollow)
                        it.dismiss()
                    },
                    {
                        // 屏蔽
                        mViewModel.onEvent(HostEvents.ClickBlock)
                        it.dismiss()
                    },
                    {
                        // report
                        it.dismiss()
                        mSecondBlockDialog.show()
                    },
                    {
                        it.dismiss()
                    }).apply {
                        show()
                }
            }
            hostDetailVideoCallBar.typeface = FontUtil.getTypeface(requireContext())
            hostDetailFollow.setOnClickListener {
                mViewModel.onEvent(HostEvents.ClickFollow)
            }
            hostDetailChatbar.setOnClickListener {
                // NOTE 跳转私聊页
                val hostInfo = mViewModel.hostInfo.value
                if (hostInfo == null) {

                }else {
                    // TODO: https://docs.rongcloud.cn/im/imlib/web/conversation/structure/会话类型要做区分：系统和用户 
                    RouteUtils.routeToConversationActivity(context, Conversation.ConversationType.PRIVATE, hostInfo.userId)
                }
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
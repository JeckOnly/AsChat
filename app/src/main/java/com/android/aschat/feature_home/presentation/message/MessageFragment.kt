package com.android.aschat.feature_home.presentation.message

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.aschat.R
import com.android.aschat.common.Constants
import com.android.aschat.databinding.HomeMessageFragmentBinding
import com.android.aschat.feature_home.presentation.HomeViewModel
import com.android.aschat.feature_home.presentation.message.calls.CallsFragment
import com.android.aschat.feature_home.presentation.message.followed.FollowedFragment
import com.android.aschat.feature_home.presentation.message.messages.MessagesFragment
import com.android.aschat.util.FontUtil
import com.android.aschat.util.LogUtil
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import io.rong.imkit.RongIM
import io.rong.imkit.config.ConversationListBehaviorListener
import io.rong.imkit.conversationlist.ConversationListFragment
import io.rong.imkit.conversationlist.model.BaseUiConversation
import io.rong.imlib.model.Conversation


@AndroidEntryPoint
class MessageFragment: Fragment() {

    private lateinit var mBinding: HomeMessageFragmentBinding
    private val mViewModel: HomeViewModel by activityViewModels()
    private lateinit var mMCFPagerAdapter: MCFPagerAdapter
    private val tabHolders: MutableList<MessageTagHolder> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = HomeMessageFragmentBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        initWidget()
    }

    fun initBinding() {

    }

    fun initWidget() {
        // 设置viewpager2
        mMCFPagerAdapter = MCFPagerAdapter(this)
        mBinding.messagePager.adapter = mMCFPagerAdapter

        // 设值tab
        mBinding.messageTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab == null) return
                setTabSelected(tabHolders[tab.position], tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                if (tab == null) return
                setTabUnSelected(tabHolders[tab.position], tab.position)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
        TabLayoutMediator(mBinding.messageTab, mBinding.messagePager) { tab, position ->
            tab.setCustomView(R.layout.home_message_tag)
            val tabHolder = MessageTagHolder(tab.customView!!)
            // 给tabholders的集合加多一个
            tabHolders.add(position, tabHolder)
            setTabUnSelected(tabHolder, position)
        }.attach()
    }

    private fun setTabUnSelected(tabHolder: MessageTagHolder, position: Int) {
        tabHolder.apply {
            setTextColor(requireContext(), R.color.tab_gray)
            setTextContent(getString(Constants.MCF_List[position]))
            setTextSize(12f)
            setTypeface(FontUtil.getTypeface(requireContext()))
        }
    }
    private fun setTabSelected(tabHolder: MessageTagHolder, position: Int) {
        tabHolder.apply {
            setTextColor(requireContext(), R.color.tab_black)
            setTextContent(getString(Constants.MCF_List[position]))
            setTextSize(18f)
            setTypeface(FontUtil.getTypeface(requireContext()))
        }
    }

    /**
     * message, call followed做分页
     */
    inner class MCFPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int {
            return Constants.MCF_List.size
        }

        override fun createFragment(position: Int): Fragment {
            when (position){
                0 -> {
//                    return MessagesFragment()
                    return ConversationListFragment()
                }
                1 -> {
                    return CallsFragment()
                }
                2 -> {
                    return FollowedFragment()
                }
            }
            // 不会来到这一步
            return ConversationListFragment()
        }
    }
}
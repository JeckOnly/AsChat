package com.android.aschat.feature_rank.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.android.aschat.R
import com.android.aschat.common.Constants
import com.android.aschat.databinding.HostShowshortvideoFragmentBinding
import com.android.aschat.databinding.RankMainFragmentBinding
import com.android.aschat.feature_home.presentation.message.MessageTagHolder
import com.android.aschat.feature_host.presentation.HostEvents
import com.android.aschat.feature_host.presentation.HostViewModel
import com.android.aschat.feature_host.presentation.hostinfo.shortvideoshow.ShowOneVideoFragment
import com.android.aschat.feature_rank.presentation.charm.RankCharmFragment
import com.android.aschat.feature_rank.presentation.rich.RankRichFragment
import com.android.aschat.util.FontUtil
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle

class RankMainFragment : Fragment() {

    private val mViewModel: RankViewModel by activityViewModels()
    private lateinit var mBinding: RankMainFragmentBinding
    private lateinit var mRankPagerAdapter: ShowRankFragmentAdapter
    private val mTabHolders: MutableList<RankTagHolder> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = RankMainFragmentBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initWidget()
    }


    private fun initWidget() {
        // 设置viewpager2
        mRankPagerAdapter = ShowRankFragmentAdapter(this)
        mBinding.rankMainPager.adapter = mRankPagerAdapter

        mBinding.apply {
            // 设值tab
            rankMainTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab == null) return
                    setTabSelected(mTabHolders[tab.position], tab.position)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    if (tab == null) return
                    setTabUnSelected(mTabHolders[tab.position], tab.position)
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }
            })
            TabLayoutMediator(mBinding.rankMainTab, mBinding.rankMainPager) { tab, position ->
                tab.setCustomView(R.layout.rank_main_tab)
                val tabHolder = RankTagHolder(tab.customView!!)
                // 给tabholders的集合加多一个
                mTabHolders.add(position, tabHolder)
                setTabUnSelected(tabHolder, position)
            }.attach()

            rankMainBack.setOnClickListener {
                mViewModel.onEvent(RankEvents.ExitRankMainFragment(requireActivity() as RankActivity))
            }
        }

        mViewModel.rankCharmData.observe(viewLifecycleOwner) {
            // 设置月份
            mBinding.rankMainMonth.text = it.monthName
        }
    }
    private fun setTabUnSelected(tabHolder: RankTagHolder, position: Int) {
        tabHolder.apply {
            setTextColor(requireContext(), R.color.tab_gray)
            setTextContent(getString(Constants.RANK_List[position]))
            setTextSize(12f)
            setTypeface(FontUtil.getTypeface(requireContext()))
        }
    }
    private fun setTabSelected(tabHolder: RankTagHolder, position: Int) {
        tabHolder.apply {
            setTextColor(requireContext(), R.color.tab_black)
            setTextContent(getString(Constants.RANK_List[position]))
            setTextSize(18f)
            setTypeface(FontUtil.getTypeface(requireContext()))
        }
    }

    inner class ShowRankFragmentAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            return if (position == 0) RankCharmFragment() else RankRichFragment()
        }
    }
}
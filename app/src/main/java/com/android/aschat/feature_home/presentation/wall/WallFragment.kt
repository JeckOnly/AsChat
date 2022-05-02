package com.android.aschat.feature_home.presentation.wall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.aschat.R
import com.android.aschat.databinding.HomeWallFragmentBinding
import com.android.aschat.feature_home.presentation.HomeEvents
import com.android.aschat.feature_home.presentation.HomeViewModel
import com.android.aschat.feature_home.presentation.wall.category.TagHolder
import com.android.aschat.feature_home.presentation.wall.category.WallCategoryFragment
import com.android.aschat.util.FontUtil
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WallFragment: Fragment() {

    private lateinit var mBinding: HomeWallFragmentBinding
    private val mViewModel: HomeViewModel by activityViewModels()
    private lateinit var mWallPagerAdapter: WallPagerAdapter
    private val tabHolders: MutableList<TagHolder> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = HomeWallFragmentBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setTypeface()
        initBinding()
        initWidget()
    }

    fun initBinding() {


    }

    fun initWidget() {
        // 设置viewpager2
        mWallPagerAdapter = WallPagerAdapter(this)
        mBinding.wallPager.adapter = mWallPagerAdapter

        mBinding.apply {
            // 设值tab
            wallTab.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
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

            // 跳转rank
            wallRanking.setOnClickListener {
                mViewModel.onEvent(HomeEvents.ClickRank)
            }
        }
        TabLayoutMediator(mBinding.wallTab, mBinding.wallPager) { tab, position ->
            tab.setCustomView(R.layout.home_wall_tab)
            val tabHolder = TagHolder(tab.customView!!)
            // 给tabholders的集合加多一个
            tabHolders.add(position, tabHolder)
            setTabUnSelected(tabHolder, position)
        }.attach()
    }

    private fun setTabUnSelected(tabHolder: TagHolder, position: Int) {
        tabHolder.apply {
            setTextColor(requireContext(), R.color.tab_gray)
            setTextContent(mViewModel.parentTagList[position].tagName)
            setTextSize(12f)
            setTypeface(FontUtil.getTypeface(requireContext()))
        }
    }
    private fun setTabSelected(tabHolder: TagHolder, position: Int) {
        tabHolder.apply {
            setTextColor(requireContext(), R.color.tab_black)
            setTextContent(mViewModel.parentTagList[position].tagName)
            setTextSize(18f)
            setTypeface(FontUtil.getTypeface(requireContext()))
        }
    }

    /**
     * 每一个category做分页
     */
    inner class WallPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int {
            return mViewModel.parentTagList.size
        }

        override fun createFragment(position: Int): Fragment {
            return WallCategoryFragment(mViewModel.parentTagList[position])
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        mBinding = null
    }
}
package com.android.aschat.feature_home.presentation.wall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.aschat.databinding.HomeWallFragmentBinding
import com.android.aschat.feature_home.presentation.HomeViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WallFragment: Fragment() {

    private lateinit var mBinding: HomeWallFragmentBinding
    private val mViewModel: HomeViewModel by activityViewModels()

    // TODO: 使用viewpager 
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
        TabLayoutMediator(mBinding.wallTab, mBinding.wallPager) { tab, position ->
            tab.text = "OBJECT ${(position + 1)}"
        }.attach()
    }
}
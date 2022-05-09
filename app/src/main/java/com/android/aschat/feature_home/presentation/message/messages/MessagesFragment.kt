package com.android.aschat.feature_home.presentation.message.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.aschat.common.Constants
import com.android.aschat.databinding.HomeMessageFragmentBinding
import com.android.aschat.databinding.HomeMessageMessageFragmentBinding
import com.android.aschat.databinding.HomeWallFragmentBinding
import com.android.aschat.feature_home.presentation.HomeViewModel
import com.android.aschat.feature_home.presentation.wall.WallFragment
import com.android.aschat.feature_home.presentation.wall.category.TagHolder
import com.android.aschat.feature_home.presentation.wall.category.WallCategoryFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
@Deprecated("用了融云那边的UI，这个不用了")
class MessagesFragment: Fragment() {

    private lateinit var mBinding: HomeMessageMessageFragmentBinding
    private val mViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = HomeMessageMessageFragmentBinding.inflate(inflater)
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

    }
}
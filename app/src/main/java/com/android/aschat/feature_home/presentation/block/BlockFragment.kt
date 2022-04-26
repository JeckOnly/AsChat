package com.android.aschat.feature_home.presentation.block

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.android.aschat.R
import com.android.aschat.databinding.HomeBlockedFragmentBinding
import com.android.aschat.feature_home.domain.model.blocked.BlockedItem
import com.android.aschat.feature_home.presentation.HomeEvents
import com.android.aschat.feature_home.presentation.HomeViewModel
import com.drake.brv.utils.linear
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BlockFragment : Fragment() {

    private lateinit var mBinding: HomeBlockedFragmentBinding
    private val mViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = HomeBlockedFragmentBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        initWidget()
    }

    fun initBinding() {
        mViewModel.apply {
            blockList.observe(viewLifecycleOwner) {
                mBinding.blcokRv.models = it
            }
            onEvent(HomeEvents.LoadBlockList)
        }
        mBinding.apply {
            blcokRv.linear().setup {
                addType<BlockedItem>(R.layout.home_blocked_item)
                onClick(R.id.block_item) {
                    mViewModel.onEvent(HomeEvents.ClickBlockItem(this.getModel()))
                }
            }
            blockedBack.setOnClickListener {
                mViewModel.onEvent(HomeEvents.ExitBlock(findNavController()))
            }
        }
    }

    fun initWidget() {

    }
}
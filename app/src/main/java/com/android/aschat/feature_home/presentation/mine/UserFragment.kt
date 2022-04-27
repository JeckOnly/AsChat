package com.android.aschat.feature_home.presentation.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.android.aschat.databinding.HomeUserFragmentBinding
import com.android.aschat.feature_home.domain.rv.usersetting.UserSettingRvAdapter
import com.android.aschat.feature_home.presentation.HomeViewModel
import com.android.aschat.feature_home.presentation.HomeEvents
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment: Fragment() {

    private lateinit var mBinding: HomeUserFragmentBinding
    private val mViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = HomeUserFragmentBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        initWidget()
    }

    fun initBinding() {
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.viewModel = mViewModel
        mBinding.adapter = UserSettingRvAdapter(findNavController())
    }

    fun initWidget() {
        mViewModel.avatarUrl.observe(viewLifecycleOwner){ avatarUrl ->
            mBinding.homeUserHead.load(avatarUrl)
        }
        mViewModel.nickName.observe(viewLifecycleOwner){ nickName ->
            mBinding.homeUserNameTv.text = nickName
        }
        mBinding.homeUserNameArea.setOnClickListener {
            mViewModel.onEvent(HomeEvents.ToEditFragment(findNavController()))
        }
    }
}

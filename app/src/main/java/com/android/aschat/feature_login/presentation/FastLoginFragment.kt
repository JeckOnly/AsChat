package com.android.aschat.feature_login.presentation

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.android.aschat.R
import com.android.aschat.databinding.LoginFastloginFragmentBinding
import com.android.aschat.util.FontUtil
import dagger.hilt.android.AndroidEntryPoint


/**
 * 第一次进入时的快速登录界面
 */
@AndroidEntryPoint
class FastLoginFragment: Fragment() {

    private val mViewModel: LoginViewModel by activityViewModels()
    private lateinit var mBinding: LoginFastloginFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = LoginFastloginFragmentBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTypeface()
        initBinding()
        initWidget()
    }

    private fun setTypeface() {
        mBinding.fastloginName.typeface = FontUtil.getTypeface(requireContext())
        mBinding.fastloginCommit.typeface = FontUtil.getTypeface(requireContext())
    }

    private fun initBinding() {
        mBinding.lifecycleOwner = viewLifecycleOwner
    }

    private fun initWidget() {
        mBinding.fastloginAgree.text = Html.fromHtml(getString(R.string.argree))
        mBinding.fastloginCommit.setOnClickListener {
            mViewModel.onEvent(LoginEvents.FastLogin2HomeActivity(requireContext(), findNavController()))
        }
    }
}
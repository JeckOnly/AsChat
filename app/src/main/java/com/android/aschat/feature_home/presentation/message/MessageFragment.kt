package com.android.aschat.feature_home.presentation.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.aschat.databinding.HomeMessageFragmentBinding
import com.android.aschat.feature_home.presentation.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MessageFragment: Fragment() {

    private lateinit var mBinding: HomeMessageFragmentBinding
    private val mViewModel: HomeViewModel by activityViewModels()

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
//        setTypeface()
        initBinding()
        initWidget()
    }

    fun initBinding() {


    }

    fun initWidget() {

    }
}
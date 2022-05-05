package com.android.aschat.feature_home.presentation.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.android.aschat.databinding.HomeAboutFragmentBinding
import com.android.aschat.feature_home.presentation.HomeEvents
import com.android.aschat.feature_home.presentation.HomeViewModel
import com.android.aschat.util.AppUtil
import com.android.aschat.util.FontUtil
import com.android.aschat.util.LogUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutFragment: Fragment() {
    private lateinit var mBinding: HomeAboutFragmentBinding
    private val mViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = HomeAboutFragmentBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        initWidget()
    }

    fun initBinding() {
        mViewModel.apply {

        }
        mBinding.apply {
            aboutVersion.text = AppUtil.getAppVersionName(requireContext())
            aboutName.typeface = FontUtil.getTypeface(requireContext())
            aboutConditionArea.setOnClickListener {
                LogUtil.d("点击condition area")
            }
            aboutPrivacyArea.setOnClickListener {
                LogUtil.d("点击privacy area")
            }
            aboutRateArea.setOnClickListener {
                LogUtil.d("点击rate area")
            }
            aboutBack.setOnClickListener {
                mViewModel.onEvent(HomeEvents.ExitAbout(findNavController()))
            }
        }
    }

    fun initWidget() {

    }
}
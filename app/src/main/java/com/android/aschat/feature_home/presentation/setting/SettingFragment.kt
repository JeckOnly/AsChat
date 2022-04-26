package com.android.aschat.feature_home.presentation.setting

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.android.aschat.R
import com.android.aschat.databinding.HomeSettingFragmentBinding
import com.android.aschat.feature_home.presentation.HomeEvents
import com.android.aschat.feature_home.presentation.HomeViewModel
import com.android.aschat.util.AppUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : Fragment() {

    private lateinit var mBinding: HomeSettingFragmentBinding
    private val clipboardManager: ClipboardManager by lazy {
        requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }
    private val mViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = HomeSettingFragmentBinding.inflate(inflater)
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
            val id = AppUtil.getAndroidId(requireContext())
            settingIdText.text = "ID:$id"
            settingIdCopy.setOnClickListener {
                clipboardManager.text = id
                Toast.makeText(requireContext(), "$id" + getString(R.string.copy_success), Toast.LENGTH_SHORT).show()
            }
            settingBack.setOnClickListener {
                mViewModel.onEvent(HomeEvents.ExitSetting(findNavController()))
            }
        }
    }

    fun initWidget() {

    }
}
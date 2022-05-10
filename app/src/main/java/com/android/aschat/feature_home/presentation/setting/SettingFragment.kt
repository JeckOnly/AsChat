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
import com.android.aschat.common.MyApplication
import com.android.aschat.common.network.translate.Translate
import com.android.aschat.databinding.HomeSettingFragmentBinding
import com.android.aschat.feature_home.presentation.HomeEvents
import com.android.aschat.feature_home.presentation.HomeViewModel
import com.android.aschat.feature_rongyun.rongyun.ui.MyTextMessageProvider
import com.android.aschat.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : Fragment() {

    private lateinit var mBinding: HomeSettingFragmentBinding
    private val clipboardManager: ClipboardManager by lazy {
        requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }
    private val mViewModel: HomeViewModel by activityViewModels()
    private lateinit var translateKey: String

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
            settingTranslateSwitch.apply {
                setOnCheckedChangeListener { buttonView, isChecked ->
                    // 写入是否自动翻译
                    SpUtil.putAndApply(requireContext(), SpConstants.Auto_Translate, isChecked)
                    MyTextMessageProvider.setNeedAutoTranslate(isChecked)
                }
                isChecked = SpUtil.get(MyApplication.application, SpConstants.Auto_Translate, false) as Boolean
            }
        }
    }

    fun initWidget() {
        translateKey = SpUtil.get(requireContext(), SpConstants.Microsoft_Translation_Key, "") as String
    }
}
package com.android.aschat.feature_home.presentation.mine

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.android.aschat.R
import com.android.aschat.databinding.HomeUserEditFragmentBinding
import com.android.aschat.feature_home.domain.model.mine.EditDetail
import com.android.aschat.feature_home.presentation.HomeEvents
import com.android.aschat.feature_home.presentation.HomeViewModel
import com.android.aschat.util.FontUtil
import com.android.aschat.util.LogUtil
import com.android.aschat.util.MobileButlerUtil
import com.android.aschat.util.RadioUtil

class UserEditFragment: Fragment() {

    private lateinit var mBinding: HomeUserEditFragmentBinding
    private val mViewModel: HomeViewModel by activityViewModels()

    /**
     * 进入相册选择照片
     */
    private val mGetImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        LogUtil.d(uri.toString())
    }

    /**
     * Android11 请求存储权限
     */
    private val mGetStoragePermissionOver11 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (MobileButlerUtil.checkExternalStorageInAllAndroid(requireContext())) {
            // 有权限，去相册选择照片
            getImageFromGallery()
        }else {
            // 算了，什么也不做

        }
    }

    /**
     * Android 11以下，请求权限，正常请求
     */
    private val mGetStoragePermissionLess11 = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            // 有权限，去相册选择照片
            getImageFromGallery()
        }else {
            if(!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // 当点击了不再询问之后，shouldShowRequestPermissionRationale返回false
                val intent = MobileButlerUtil.getDefaultSettingIntent(requireContext())
                mGetStoragePermissionLess11Rational.launch(intent)
            }else {
                // 算了

            }
        }
    }

    /**
     * Android 11以下，请求权限，点了不正常询问之后的请求
     */
    private val mGetStoragePermissionLess11Rational = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (MobileButlerUtil.checkExternalStorageInAllAndroid(requireContext())) {
            // 有权限，去相册选择照片
            getImageFromGallery()
        }else {
            // 算了，什么也不做

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = HomeUserEditFragmentBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRadio()
        setTypeface()
        initBinding()
        initWidget()
    }

    private fun setRadio() {

        RadioUtil.setBounds(requireContext(), R.drawable.style_radio, mBinding.userEditGenderMan)
        RadioUtil.setBounds(requireContext(), R.drawable.style_radio, mBinding.userEditGenderWoman)
    }

    private fun setTypeface() {
        mBinding.editSubmit.typeface = FontUtil.getTypeface(requireContext())
    }

    private fun initBinding() {
        mBinding.viewmodel = mViewModel
        mBinding.lifecycleOwner = viewLifecycleOwner
    }

    private fun initWidget() {
        mBinding.userEditBirthday.setOnClickListener {
           mViewModel.onEvent(HomeEvents.ShowTimePicker(parentFragmentManager))
        }
        mBinding.userEditCountry.setOnClickListener {
            mViewModel.onEvent(HomeEvents.ShowCountryPicker(parentFragmentManager))
        }
        mBinding.userEditGenderMan.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mBinding.editHead1.setImageResource(R.mipmap.ic_head_1)
                mBinding.editHead2.setImageResource(R.mipmap.ic_head_2)
                mBinding.editHead3.setImageResource(R.mipmap.ic_head_3)
            }else {
                mBinding.editHead1.setImageResource(R.mipmap.ic_head_4)
                mBinding.editHead2.setImageResource(R.mipmap.ic_head_5)
                mBinding.editHead3.setImageResource(R.mipmap.ic_head_6)
            }
        }
        mBinding.editHead0.setOnClickListener {
            mViewModel.onEvent(HomeEvents.ChangeHead(0))
            getImageFromGallery()
        }
        mBinding.editHead1.setOnClickListener {
            mViewModel.onEvent(HomeEvents.ChangeHead(1))
        }
        mBinding.editHead2.setOnClickListener {
            mViewModel.onEvent(HomeEvents.ChangeHead(2))
        }
        mBinding.editHead3.setOnClickListener {
            mViewModel.onEvent(HomeEvents.ChangeHead(3))
        }
        mBinding.editSubmit.setOnClickListener {
            mViewModel.onEvent(
                HomeEvents.SubmitEdit(
                findNavController(),
                EditDetail(
                    nickName = mBinding.userEditName.text.toString(),
                    birthday = mBinding.userEditBirthday.text.toString(),
                    country = mBinding.userEditCountry.text.toString(),
                    inviteCode = mBinding.userEditInvite.text.toString(),
                    gender = if (mBinding.userEditGenderMan.isChecked) 1 else 2,
                    head = 0,// 使用viewmodel中存的
                    about = mBinding.editAbout.text.toString()
                )
            ))
        }
        mBinding.editAbout.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            var mNumberCount = 0
            val mNumberCountUp = 300

            override fun afterTextChanged(editable: Editable?) {
                mNumberCount = editable!!.length // 总长度

                if (mNumberCount > mNumberCountUp) {
                    editable.delete(mNumberCountUp, mNumberCount)// 删除多余的字符
                    mNumberCount = mNumberCountUp
                }

                mBinding.editAboutCount.text = "$mNumberCount/$mNumberCountUp"
            }
        })
        mBinding.editHead0.load(mViewModel.userInfo.value!!.avatarUrl)

        mBinding.userEditBack.setOnClickListener {
            mViewModel.onEvent(HomeEvents.ExitUserEditFragment(findNavController()))
        }
    }

    private fun getImageFromGallery() {
        if (MobileButlerUtil.checkExternalStorageInAllAndroid(requireContext())) {
            // 去选择照片
            mGetImageLauncher.launch("image/*")
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // > Android 11
            mGetStoragePermissionOver11.launch(MobileButlerUtil.getAndroid11ExternalIntent(requireContext()))
        }else {
            // < Android 11
            mGetStoragePermissionLess11.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }
}

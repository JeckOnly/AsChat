package com.android.aschat.feature_home.presentation.mine

import android.Manifest
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.android.aschat.R
import com.android.aschat.databinding.HomeUserFragmentBinding
import com.android.aschat.feature_home.domain.rv.usersetting.UserSettingRvAdapter
import com.android.aschat.feature_home.presentation.HomeViewModel
import com.android.aschat.feature_home.presentation.HomeEvents
import com.android.aschat.util.DialogUtil
import com.android.aschat.util.ImageUtil
import com.android.aschat.util.MobileButlerUtil
import com.android.aschat.util.UriUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment: Fragment() {

    private lateinit var mBinding: HomeUserFragmentBinding
    private val mViewModel: HomeViewModel by activityViewModels()

    private val mLoadingDialog: Dialog by lazy {
        DialogUtil.createLoadingDialog(requireContext())
    }

    /**
     * 进入相册选择照片
     */
    private val mGetImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri == null) return@registerForActivityResult
        // 把照片更新
        mBinding.homeUserHead.load(uri)
        // 更新字段
        val originalPath = UriUtils.getFileAbsolutePath(requireContext(), uri)!!
        val avatarSrcPath = ImageUtil.compress(originalPath)
        if (avatarSrcPath != null) {
            mViewModel.onEvent(HomeEvents.UploadImageToOss(
                filePath = avatarSrcPath,
                onStartSubmit = {
                    mLoadingDialog.show()
                },
                onSuccess = {
                    Toast.makeText(requireContext(), getString(R.string.Save_avatar_successfully), Toast.LENGTH_SHORT).show()
                    mLoadingDialog.dismiss()
                },
                onFail = {
                    Toast.makeText(requireContext(), getString(R.string.Failed_to_save_avatar), Toast.LENGTH_SHORT).show()
                    mLoadingDialog.dismiss()
                }
            ))
        }
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
    private val mGetStoragePermissionLess11Rational = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
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
        mBinding.homeUserHead.setOnClickListener {
            getImageFromGallery()
        }
    }

    /**
     * 前往相册选择照片
     */
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

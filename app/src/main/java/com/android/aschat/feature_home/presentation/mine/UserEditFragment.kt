package com.android.aschat.feature_home.presentation.mine

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.android.aschat.R
import com.android.aschat.databinding.HomeUserEditFragmentBinding
import com.android.aschat.feature_home.domain.model.EditDetail
import com.android.aschat.feature_home.presentation.HomeViewModel
import com.android.aschat.util.FontUtil
import com.android.aschat.util.RadioUtil

class UserEditFragment: Fragment() {

    private lateinit var mBinding: HomeUserEditFragmentBinding
    private val mViewModel: HomeViewModel by activityViewModels()

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
           mViewModel.onEvent(UserEvents.ShowTimePicker(parentFragmentManager))
        }
        mBinding.userEditCountry.setOnClickListener {
            mViewModel.onEvent(UserEvents.ShowCountryPicker(parentFragmentManager))
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
            mViewModel.onEvent(UserEvents.ChangeHead(0))
        }
        mBinding.editHead1.setOnClickListener {
            mViewModel.onEvent(UserEvents.ChangeHead(1))
        }
        mBinding.editHead2.setOnClickListener {
            mViewModel.onEvent(UserEvents.ChangeHead(2))
        }
        mBinding.editHead3.setOnClickListener {
            mViewModel.onEvent(UserEvents.ChangeHead(3))
        }
        mBinding.editSubmit.setOnClickListener {
            mViewModel.onEvent(UserEvents.SubmitEdit(
                findNavController(),
                EditDetail(
                    nickName = mBinding.userEditName.text.toString(),
                    birthday = mBinding.userEditBirthday.text.toString(),
                    country = mBinding.userEditCountry.text.toString(),
                    inviteCode = mBinding.userEditInvite.text.toString(),
                    gender = if (mBinding.userEditGenderMan.isChecked) 1 else 0,
                    head = 0,// 使用viewmodel中存的
                    about = mBinding.editAbout.text.toString()
                )))
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
        // TODO: Jeck 点击head0可以进入相册更改照片
        mBinding.editHead0.load(mViewModel.userInfo.value!!.avatarUrl)

        mBinding.userEditBack.setOnClickListener {
            mViewModel.onEvent(UserEvents.ExitUserEditFragment(findNavController()))
        }
    }
}

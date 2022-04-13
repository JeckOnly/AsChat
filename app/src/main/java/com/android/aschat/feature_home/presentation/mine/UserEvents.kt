package com.android.aschat.feature_home.presentation.mine

import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import com.android.aschat.feature_home.domain.model.EditDetail

sealed class UserEvents {
    class SubmitEdit(val navController: NavController, val editDetail: EditDetail): UserEvents()
    class ToEditFragment(val navController: NavController):UserEvents()
    class ShowTimePicker(val fm: FragmentManager): UserEvents()
    class ShowCountryPicker(val fm: FragmentManager): UserEvents()
    class ChangeHead(val head:Int): UserEvents()

    class ExitUserEditFragment(val navController: NavController): UserEvents()
}
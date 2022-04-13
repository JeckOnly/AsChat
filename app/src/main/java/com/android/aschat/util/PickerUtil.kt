package com.android.aschat.util

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.android.aschat.R
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.yesterselga.countrypicker.CountryPicker
import com.yesterselga.countrypicker.Theme
import java.util.*

object PickerUtil {


    /**
     * 月份记得+1
     *
     *  PickerUtil.showTimePicker(parentFragmentManager){dialog, y, m, d ->
     *   Log.d("Jeck", "$y, $m+1, $d")
     *       }
     */
    fun showTimePicker(fm: FragmentManager, setTimeCallback: (DatePickerDialog, Int, Int, Int) -> Unit) {
        val calendar = Calendar.getInstance()

        val dialog = DatePickerDialog.newInstance(
            setTimeCallback,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
        )
        dialog.show(fm, "time dialog")
    }

    /**
     * PickerUtil.showCountryPicker(requireContext()) {
     * if (it != null) {
     * Log.d("Jeck", "${it.cctld} ${it.englishName}")// cn China
     * }
     * }
     */
    fun showCountryPicker(context: Context, fm: FragmentManager, setCountryCallback: (String, String, String, Int)->Unit) {
       CountryPicker.newInstance(context.getString(R.string.select_country), Theme.LIGHT).apply {
           setListener { name,  code,  dialCode,  flagDrawableResID ->
               setCountryCallback(name,  code,  dialCode,  flagDrawableResID)
               this.dismiss()
           }
           show(fm, "dialog tag")
       }
    }

}
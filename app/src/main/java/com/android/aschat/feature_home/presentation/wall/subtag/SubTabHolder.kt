package com.android.aschat.feature_home.presentation.wall.subtag

import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.android.aschat.R

/**
 * 目前和tabholder一样，如果要改，可以不影响tabholder
 */
class SubTabHolder (private val tabView: View) {
    private val mText: TextView = tabView.findViewById(R.id.home_wall_tab_text)

    fun setTextSize(sp: Float) {
        mText.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp)
    }
    fun setTextColor(context: Context, colorId: Int) {
        mText.setTextColor(context.resources.getColor(colorId))
    }
    fun setTextContent(text: CharSequence) {
        mText.text = text
    }
    fun setTypeface(typeFace: Typeface) {
        mText.typeface = typeFace
    }
    fun setTextWeight(style: Int) {
        mText.setTypeface(null, style)
    }
}
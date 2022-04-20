package com.android.aschat.feature_home.presentation.wall.category

import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.android.aschat.R

class TagHolder(private val tabView: View) {
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
}
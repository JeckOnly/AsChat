package com.android.aschat.feature_rank.presentation

import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.android.aschat.R

class RankTagHolder(private val tagView: View) {
    private val mText: TextView = tagView.findViewById(R.id.rank_main_tab_text)

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
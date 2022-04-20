package com.android.aschat.feature_home.presentation.message

import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.android.aschat.R

class MessageTagHolder(private val tagView: View) {
    private val mText: TextView = tagView.findViewById(R.id.home_message_tag_text)
    private val mRed: ImageView = tagView.findViewById(R.id.home_message_tag_red)

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
    fun setRedVisibility(isVisible: Boolean) {
        if (isVisible)
            mRed.visibility = View.VISIBLE
        else
            mRed.visibility = View.INVISIBLE
    }
}
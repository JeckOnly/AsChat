package com.android.aschat.feature_host.domain.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.FontMetricsInt
import android.util.AttributeSet
import androidx.annotation.Nullable
import com.android.aschat.util.DensityUtil
import com.zhpan.indicator.base.BaseIndicatorView


class NumberIndicator(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) :
    BaseIndicatorView(context, attrs, defStyleAttr) {

    constructor(context: Context): this(context, null, 0)
    constructor(context: Context, @Nullable attrs: AttributeSet?): this(context, attrs, 0)

    private var textColor = Color.BLACK
    private var textSize: Float = DensityUtil.sp2px(18f, context) * 1f
    private val mPaint = Paint()

    private var initPage = -1

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(7*textSize.toInt(), textSize.toInt())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mPaint.isAntiAlias = true// 抗锯齿
        mPaint.color = textColor
        mPaint.textSize = textSize
        var text = ""
        if (initPage == -1) {
            text = "${getCurrentPosition()+1}/${getPageSize()}"
        }else {
            text = "${initPage+1}/${getPageSize()}"
            initPage = -1
        }
        val textWidth = mPaint.measureText(text).toInt()
        val fontMetricsInt: FontMetricsInt = mPaint.fontMetricsInt
        val baseline =
            (measuredHeight - fontMetricsInt.bottom + fontMetricsInt.top) / 2 - fontMetricsInt.top
        canvas.drawText(text, ((width - textWidth) / 2).toFloat(), baseline.toFloat(), mPaint)
    }

    fun setInitPage(position: Int) {
        initPage = position
    }
}

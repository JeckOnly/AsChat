package com.android.aschat.util

import android.content.Context
import android.graphics.drawable.Drawable

import android.widget.RadioButton




object RadioUtil {
    /**
     *
     * @param drawableId  drawableLeft  drawableTop drawableBottom 所用的选择器 通过R.drawable.xx 获得
     * @param radioButton  需要限定图片大小的radioButton
     */
    fun setBounds(context: Context, drawableId: Int, radioButton: RadioButton) {
        //定义底部标签图片大小和位置
        val drawable_news: Drawable = context.resources.getDrawable(drawableId)
        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形  (这里的长和宽写死了 自己可以可以修改成 形参传入)
        drawable_news.setBounds(0, 0, 50, 50)
        //设置图片在文字的哪个方向
        radioButton.setCompoundDrawables(drawable_news, null, null, null)
    }
}
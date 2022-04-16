package com.android.aschat.feature_home.domain.rv

import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.android.aschat.R
import com.android.aschat.feature_home.domain.model.mine.HomeUserListItem
import com.android.aschat.util.LogUtil

@BindingAdapter("setAdapter")
fun setAdapter(rv: RecyclerView, adapter: UserSettingRvAdapter) {
    rv.adapter = adapter
}

@BindingAdapter("submitList")
fun submitList(rv: RecyclerView, data: MutableList<HomeUserListItem>?) {
    if (rv.adapter != null) {
        (rv.adapter as UserSettingRvAdapter).apply {
            submitList(data?.toMutableList())
        }

    }
}

@BindingAdapter("setImageUrl")
fun setImageUrl(imageView: ImageView, url: String?) {
    if (url == null) {
        LogUtil.d("url是null")
        return
    }
    imageView.load(url)
}

@BindingAdapter("setHostTag")
fun setHostTag(textView: TextView, tags: List<String>?) {
    if (tags == null || tags.isEmpty()) return
    textView.text = tags[0]
}

@BindingAdapter("setHostStatus")
fun setHostStatus(imageView: ImageView, status: String) {
    when (status) {
        "Online" -> {
            imageView.setBackgroundResource(R.drawable.shape_green_fill)
        }
        "Busy" -> {
            imageView.setBackgroundResource(R.drawable.shape_yellow_fill)
        }
        "Incall" -> {
            imageView.setBackgroundResource(R.drawable.shape_red_fill)
        }
        "Offline" -> {
            imageView.setBackgroundResource(R.drawable.shape_gray_fill)
        }
        "Available" -> {
            imageView.setBackgroundResource(R.drawable.shape_green_fill)
        }
        else -> {
            imageView.setBackgroundResource(R.drawable.shape_gray_fill)
        }
    }
}

@BindingAdapter("setTextViewInt")
fun setTextViewInt(textView: TextView, int: Int) {
    textView.text = int.toString()
}
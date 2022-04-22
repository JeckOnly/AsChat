package com.android.aschat.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.videoFrameMillis
import com.android.aschat.R
import com.android.aschat.common.Constants
import com.android.aschat.common.Gift
import com.android.aschat.common.gift2ImageId
import com.android.aschat.feature_home.domain.model.mine.HomeUserListItem
import com.android.aschat.feature_home.domain.rv.UserSettingRvAdapter
import com.android.aschat.feature_host.domain.customview.FlowLayout
import com.android.aschat.feature_host.domain.rv.HostDetailLabelRvAdapter
import com.android.aschat.feature_host.domain.rv.HostDetailVideoRvAdapter


@BindingAdapter("setAdapter")
fun setAdapter(rv: RecyclerView, adapter: UserSettingRvAdapter) {
    rv.adapter = adapter
}

@BindingAdapter("setHostVideoAdapter")
fun setHostVideoAdapter(rv: RecyclerView, adapter: HostDetailVideoRvAdapter) {
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

@BindingAdapter("submitListStr")
fun submitListStr(rv: RecyclerView, data: MutableList<String>?) {
    if (rv.adapter != null) {
        (rv.adapter as HostDetailVideoRvAdapter).apply {
            submitList(data?.toMutableList())
        }
    }
}

@BindingAdapter("submitListStrLabel")
fun submitListStrLabel(rv: RecyclerView, data: MutableList<String>?) {
    if (rv.adapter != null) {
        (rv.adapter as HostDetailLabelRvAdapter).apply {
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

@BindingAdapter("setGiftImage")
fun setGiftImage(imageView: ImageView, gift: Gift?) {
    if (gift == null) return
    imageView.setImageResource(gift2ImageId(gift))
}

@BindingAdapter("setGiftNumber")
fun setGiftNumber(textView: TextView, giftNumber: String?) {
    if (giftNumber == null) return
    textView.text = "x$giftNumber"
}

@BindingAdapter("setHostTag")
fun setHostTag(textView: TextView, tags: List<String>?) {
    if (tags == null || tags.isEmpty()) return
    textView.text = tags[0]
}

@BindingAdapter("setHostStatus")
fun setHostStatus(imageView: ImageView, status: String?) {
    if (status == null) {
        imageView.setBackgroundResource(R.drawable.shape_gray_fill)
        return
    }
    when (status) {
        Constants.Online -> {
            imageView.setBackgroundResource(R.drawable.shape_green_fill)
        }
        Constants.Busy-> {
            imageView.setBackgroundResource(R.drawable.shape_yellow_fill)
        }
        Constants.Incall -> {
            imageView.setBackgroundResource(R.drawable.shape_red_fill)
        }
        Constants.Offline -> {
            imageView.setBackgroundResource(R.drawable.shape_gray_fill)
        }
        Constants.Avaliable -> {
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

@BindingAdapter("setVideoFirstFrame")
fun setVideoFirstFrame(imageView: ImageView, link: String) {
    imageView.load(link){
        videoFrameMillis(1000L)
    }
}

@BindingAdapter("setFlowData")
fun setFlowData(flowLayout: FlowLayout, data: List<String>?) {
    if (data != null) {
        // 设置对齐方式
        flowLayout.setAlignByCenter(FlowLayout.AlienState.LEFT)
        flowLayout.setAdapter(
            data,
            R.layout.host_detail_label_item,
            object : FlowLayout.ItemView<String>() {
                override fun <String> getCover(
                    item: String,
                    holder: FlowLayout.ViewHolder?,
                    inflate: View?,
                    position: Int
                ) {
                    holder?.setText(R.id.host_detail_label_item_text, item);
                }
            })
    }
}

@BindingAdapter("setVisibilityByListSize")
fun setVisibilityByListSize(view: View, data: List<*>?) {
    if (data == null) {
        view.visibility = View.GONE
    }else if (data.isEmpty()) {
        view.visibility = View.GONE
    }else {
        view.visibility = View.VISIBLE
    }
}
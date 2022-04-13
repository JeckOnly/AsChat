package com.android.aschat.feature_home.domain.rv

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.aschat.feature_home.domain.model.HomeUserListItem

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
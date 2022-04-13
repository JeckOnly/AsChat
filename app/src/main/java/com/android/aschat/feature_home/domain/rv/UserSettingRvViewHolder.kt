package com.android.aschat.feature_home.domain.rv

import androidx.recyclerview.widget.RecyclerView
import com.android.aschat.databinding.HomeUserListItemBinding
import com.android.aschat.feature_home.domain.model.HomeUserListItem

class UserSettingRvViewHolder(private val binding: HomeUserListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: HomeUserListItem) {
        binding.item = item
        binding.executePendingBindings()
    }
}
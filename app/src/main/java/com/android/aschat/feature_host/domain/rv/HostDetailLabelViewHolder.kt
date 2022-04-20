package com.android.aschat.feature_host.domain.rv

import androidx.recyclerview.widget.RecyclerView
import com.android.aschat.databinding.HomeUserListItemBinding
import com.android.aschat.databinding.HostDetailLabelItemBinding
import com.android.aschat.databinding.HostDetailVideoItemBinding
import com.android.aschat.feature_home.domain.model.mine.HomeUserListItem

class HostDetailLabelViewHolder(private val binding: HostDetailLabelItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: String) {
        binding.text = item
    }
}
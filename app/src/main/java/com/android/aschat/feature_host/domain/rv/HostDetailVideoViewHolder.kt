package com.android.aschat.feature_host.domain.rv

import androidx.recyclerview.widget.RecyclerView
import com.android.aschat.databinding.HomeUserListItemBinding
import com.android.aschat.databinding.HostDetailVideoItemBinding
import com.android.aschat.feature_home.domain.model.mine.HomeUserListItem

class HostDetailVideoViewHolder(private val binding: HostDetailVideoItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: String, position: Int, onClickVideo: (position: Int) -> Unit) {
        binding.imageUrl = item
        binding.root.setOnClickListener {
            onClickVideo(position)
        }
    }
}
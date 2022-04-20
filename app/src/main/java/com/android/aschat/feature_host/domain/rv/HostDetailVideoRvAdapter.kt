package com.android.aschat.feature_host.domain.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.android.aschat.databinding.HostDetailVideoItemBinding

/**
 * 主播资料页的详细信息
 */
class HostDetailVideoRvAdapter(val onClickVideo: (position: Int) -> Unit): ListAdapter<String, HostDetailVideoViewHolder>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HostDetailVideoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = HostDetailVideoItemBinding.inflate(layoutInflater, parent, false)
        return HostDetailVideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HostDetailVideoViewHolder, position: Int) {
        holder.bind(getItem(position), position) {
            onClickVideo(it)
        }
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<String>(){
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

        }
    }
}
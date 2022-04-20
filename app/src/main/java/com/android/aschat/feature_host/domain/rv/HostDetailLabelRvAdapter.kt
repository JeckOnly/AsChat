package com.android.aschat.feature_host.domain.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.android.aschat.databinding.HostDetailLabelItemBinding
import com.android.aschat.databinding.HostDetailVideoItemBinding

class HostDetailLabelRvAdapter : ListAdapter<String, HostDetailLabelViewHolder>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HostDetailLabelViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = HostDetailLabelItemBinding.inflate(layoutInflater, parent, false)
        return HostDetailLabelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HostDetailLabelViewHolder, position: Int) {
        holder.bind(getItem(position))
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
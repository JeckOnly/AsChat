package com.android.aschat.feature_home.domain.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.android.aschat.databinding.HomeUserListItemBinding
import com.android.aschat.feature_home.domain.model.mine.HomeUserListItem

class UserSettingRvAdapter: ListAdapter<HomeUserListItem, UserSettingRvViewHolder>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserSettingRvViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = HomeUserListItemBinding.inflate(layoutInflater, parent, false)
        return UserSettingRvViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserSettingRvViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<HomeUserListItem>(){
            override fun areItemsTheSame(oldItem: HomeUserListItem, newItem: HomeUserListItem): Boolean{
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: HomeUserListItem, newItem: HomeUserListItem): Boolean {
                return oldItem.text == newItem.text
            }

        }
    }
}
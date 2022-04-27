package com.android.aschat.feature_home.domain.rv.usersetting

import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.android.aschat.databinding.HomeUserListItemBinding
import com.android.aschat.feature_home.domain.model.mine.HomeUserListItem

class UserSettingRvViewHolder(private val binding: HomeUserListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(navController: NavController, item: HomeUserListItem) {
        binding.item = item
        binding.root.setOnClickListener {
            item.clickListener(navController)
        }
        binding.executePendingBindings()
    }
}
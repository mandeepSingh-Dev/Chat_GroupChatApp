package com.example.chat__groupchatapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.chat__groupchatapp.databinding.GroupItemlayoutBinding
import io.agora.chat.Group

class GroupsAdapter : ListAdapter<Group, GroupsAdapter.GroupsViewHolders>(diffUtils){

    inner class GroupsViewHolders(val binding : GroupItemlayoutBinding) : ViewHolder(binding.root){

        fun bind(group: Group)
        {
            binding.userNameTextView.text = group.groupName ?: group.groupId
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupsViewHolders {
        val binding = GroupItemlayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  GroupsViewHolders(binding)
    }

    override fun onBindViewHolder(holder: GroupsViewHolders, position: Int) {
        holder.bind(getItem(position))
    }

    object diffUtils : DiffUtil.ItemCallback<Group>(){
        override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem == newItem
        }
    }
}
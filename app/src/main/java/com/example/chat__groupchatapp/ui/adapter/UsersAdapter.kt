package com.example.chat__groupchatapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.chat__groupchatapp.data.remote.model.user.response.UserEntity
import com.example.chat__groupchatapp.databinding.UserItemlayoutBinding

class UsersAdapter(val onClick : (UserEntity) -> Unit) : ListAdapter<UserEntity, UsersAdapter.UsersViewHolders>(diffUtils){

    inner class UsersViewHolders(val binding : UserItemlayoutBinding) : ViewHolder(binding.root){

        fun bind(userEntity: UserEntity)
        {
            binding.userNameTextView.text = userEntity.nickname ?: userEntity.username

            binding.root.setOnClickListener {
                onClick(userEntity)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolders {
        val binding = UserItemlayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
       return  UsersViewHolders(binding)
    }

    override fun onBindViewHolder(holder: UsersViewHolders, position: Int) {
        holder.bind(getItem(position))
    }

    object diffUtils : DiffUtil.ItemCallback<UserEntity>(){
        override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
            return oldItem == newItem
        }
    }
}
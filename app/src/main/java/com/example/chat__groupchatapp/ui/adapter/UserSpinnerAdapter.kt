package com.example.chat__groupchatapp.ui.adapter

import android.util.Log
import com.example.chat__groupchatapp.databinding.ItemsSpinnerItemBinding
import com.example.chat__groupchatapp.domain.model.UserSpinnerItem


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.chat__groupchatapp.databinding.GroupItemlayoutBinding
import io.agora.chat.Group

class UserSpinnerAdapter(val onCheckedItem : (UserSpinnerItem, Boolean) -> Unit) : ListAdapter<UserSpinnerItem, UserSpinnerAdapter.UserSpinnerViewHolders>(diffUtils){

    inner class UserSpinnerViewHolders(val binding : ItemsSpinnerItemBinding) : ViewHolder(binding.root){

        fun bind(item: UserSpinnerItem)
        {
            binding.userTextview.text = item.userId
            binding.radioBtn.isChecked = item.isSelected


            binding.radioBtn.setOnCheckedChangeListener { buttonView, isChecked ->
                Log.d("fvkkvmfv",isChecked.toString())
                onCheckedItem(item, isChecked)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserSpinnerViewHolders {
        val binding = ItemsSpinnerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  UserSpinnerViewHolders(binding)
    }

    override fun onBindViewHolder(holder: UserSpinnerViewHolders, position: Int) {
        holder.bind(getItem(position))
    }

    object diffUtils : DiffUtil.ItemCallback<UserSpinnerItem>(){
        override fun areContentsTheSame(oldItem: UserSpinnerItem, newItem: UserSpinnerItem): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: UserSpinnerItem, newItem: UserSpinnerItem): Boolean {
            return oldItem == newItem
        }
    }
}
package com.example.chat__groupchatapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.chat__groupchatapp.databinding.GroupItemlayoutBinding
import com.example.chat__groupchatapp.databinding.GroupVideoSurfaceItemBinding
import com.example.chat__groupchatapp.ui.activities.SurfaceViewItem

class GroupVideoSurfaceViewAdapter : ListAdapter<SurfaceViewItem, GroupVideoSurfaceViewAdapter.GroupVideoViewholder>(diffUtils) {

    inner class GroupVideoViewholder(val binding : GroupVideoSurfaceItemBinding) : ViewHolder(binding.root){

        fun bind(item : SurfaceViewItem){
            binding.groupVideoFrame.addView(item.surfaceView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupVideoViewholder {
        val binding = GroupVideoSurfaceItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return GroupVideoViewholder(binding)
    }

    override fun onBindViewHolder(holder: GroupVideoViewholder, position: Int) {
     holder.bind(getItem(position))
    }

    object diffUtils : DiffUtil.ItemCallback<SurfaceViewItem>() {
        override fun areContentsTheSame(
            oldItem: SurfaceViewItem,
            newItem: SurfaceViewItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: SurfaceViewItem, newItem: SurfaceViewItem): Boolean {
            return oldItem == newItem
        }
    }
}
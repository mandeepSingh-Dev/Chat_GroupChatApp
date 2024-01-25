package com.example.chat__groupchatapp.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.chat__groupchatapp.databinding.MessageItemFromBinding
import com.example.chat__groupchatapp.databinding.MessageItemToBinding
import io.agora.chat.ChatMessage
import io.agora.chat.TextMessageBody
import java.text.SimpleDateFormat
import java.util.Calendar

class MessagesAdapter(var currentUser : String, val onLongClick : (ChatMessage) -> Unit) : androidx.recyclerview.widget.ListAdapter<ChatMessage, ViewHolder>(
    diffUtils
) {

    inner class MessagesToViewholder(val binding : MessageItemToBinding) : ViewHolder(binding.root)
    {
        fun bind(chatMessage: ChatMessage){

            Log.d("fvkmfkf",chatMessage.conversationId().toString())
            Log.d("flvmvkfmv",chatMessage.from.toString())
            Log.d("fkvkmf",chatMessage.userName.toString())
            binding.messageString.text = (chatMessage.body as TextMessageBody).message ?: "Empty"
            val cal = Calendar.getInstance()
            cal.timeInMillis = chatMessage.msgTime.toLong()
            binding.messageTimeString.text = SimpleDateFormat("hh:mm").format(cal.time)

            binding.usernameTextview.text = chatMessage.from
            if(chatMessage.chatType == ChatMessage.ChatType.GroupChat){
                binding.usernameTextview.visibility = View.VISIBLE
            }else{
                binding.usernameTextview.visibility = View.GONE
            }


            binding.root.setOnLongClickListener {
                onLongClick(chatMessage)
                return@setOnLongClickListener true
            }
        }
    }

    inner class MessagesFromViewholder(val binding : MessageItemFromBinding) : ViewHolder(binding.root)
    {
        @SuppressLint("SimpleDateFormat")
        fun bind(chatMessage: ChatMessage){
            binding.messageString.text = (chatMessage.body as TextMessageBody).message ?: "Empty"
            val cal = Calendar.getInstance()
            cal.timeInMillis = chatMessage.msgTime.toLong()
            binding.messageTimeString.text = SimpleDateFormat("hh:mm").format(cal.time)


            binding.usernameTextview.text = chatMessage.from
            if(chatMessage.chatType == ChatMessage.ChatType.GroupChat){
                binding.usernameTextview.visibility = View.VISIBLE
            }else{
                binding.usernameTextview.visibility = View.GONE
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

     var viewHolder = if(viewType == 0){
            val fromBinding = MessageItemFromBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            MessagesFromViewholder(fromBinding)
        }else{
           var toBinding =  MessageItemToBinding.inflate(LayoutInflater.from(parent.context),parent,false)
          MessagesToViewholder(toBinding)
      }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       // holder.bind(chatMessage = getItem(position))
        var item = getItem(position)
        if(holder is MessagesFromViewholder){
            holder.bind(item)
        }else if(holder is MessagesToViewholder){
            holder.bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
       return if(item.from.lowercase() != currentUser)
            0
        else
            1

    }
    object diffUtils : DiffUtil.ItemCallback<ChatMessage>(){
        override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
            return oldItem ==  newItem
        }

        override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
            return oldItem ==  newItem
        }
    }
}
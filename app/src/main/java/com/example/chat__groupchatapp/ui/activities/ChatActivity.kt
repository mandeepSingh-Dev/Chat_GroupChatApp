package com.example.chat__groupchatapp.ui.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.example.chat__groupchatapp.AgoraChatHelper
import com.example.chat__groupchatapp.Utils.Widgets.BounceButton
import com.example.chat__groupchatapp.data.remote.model.user.response.UserEntity
import com.example.chat__groupchatapp.databinding.ActivityChatBinding
import io.agora.chat.ChatMessage
import io.agora.chat.Conversation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {

    lateinit var binding : ActivityChatBinding
    var agoraChatHelper = AgoraChatHelper()

    var chat_type : Conversation.ConversationType? = null


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        agoraChatHelper.setUpChatClient(this)

        val type =  intent.getStringExtra("chat_type")
       chat_type = if(type == Conversation.ConversationType.Chat.toString()) Conversation.ConversationType.Chat else Conversation.ConversationType.GroupChat


       //When comes from User item
        val userEntity =  intent.getParcelableExtra("user",UserEntity::class.java)
        //When comes from Group item.
        val groupId = intent.getStringExtra("group_Id")
        val group_Name =   intent.getStringExtra("group_Name")
        val group_description =   intent.getStringExtra("group_description")
        val group_owner =  intent.getStringExtra("group_owner")

        binding.contactName.text = if(chat_type == Conversation.ConversationType.Chat){
             userEntity?.nickname ?: userEntity?.username
        }else{
            group_Name

        }
        lifecycleScope.launch {
            if(chat_type == Conversation.ConversationType.Chat){
                agoraChatHelper.getAsyncFetchConverationFromServer(userId = userEntity?.username.toString(), chatType = Conversation.ConversationType.Chat)
            }else{
                agoraChatHelper.getAsyncFetchConverationFromServer(userId = groupId.toString(), chatType = Conversation.ConversationType.GroupChat)

            }
        }


        binding.sendButton.setContent {
            BounceButton(buttonText = "Send") {

                val messageStr = binding.messageInputEditText.text.toString()
                Log.d("dvkndvjd",messageStr.toString())
                if(messageStr.isNotEmpty()){
                    CoroutineScope(Dispatchers.IO).launch {
                        if(chat_type == Conversation.ConversationType.Chat){
                            agoraChatHelper.sendMessage(message = messageStr, toUserId = userEntity?.username.toString(), chatType = ChatMessage.ChatType.Chat, onCreateMessage = {})

                        }else{
                            agoraChatHelper.sendMessage(message = messageStr, toUserId = groupId.toString(), chatType = ChatMessage.ChatType.GroupChat , onCreateMessage = {})
                        }
                    }
                }
            }
        }






    }
}
package com.example.chat__groupchatapp.ui.activities

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat__groupchatapp.AgoraChatHelper
import com.example.chat__groupchatapp.Utils.Widgets.BounceButton
import com.example.chat__groupchatapp.Utils.showLog
import com.example.chat__groupchatapp.Utils.showSnackbar
import com.example.chat__groupchatapp.data.remote.model.user.response.UserEntity
import com.example.chat__groupchatapp.databinding.ActivityChatBinding
import com.example.chat__groupchatapp.databinding.EditMsgDialogBinding
import com.example.chat__groupchatapp.ui.adapter.MessagesAdapter
import com.example.chat__groupchatapp.ui.dialogs.ProgressDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.agora.chat.ChatMessage
import io.agora.chat.Conversation
import io.agora.chat.TextMessageBody
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {

    private var membersList: MutableList<String>? = mutableListOf()
    private  lateinit var binding : ActivityChatBinding
    private  val progressDialog: ProgressDialog by lazy {
        ProgressDialog()
    }


    private var chat_type : Conversation.ConversationType? = null
    private var userEntity : UserEntity? = null
    private  var groupId : String? = null
    private var group_Name : String? = null
    private  var group_description : String? = null
    private  var group_owner : String? = null

    private var  messagesAdapter : MessagesAdapter ? = null
    private var currentUser : String = ""


    private   var agoraChatHelper = AgoraChatHelper(
        onMessageRecievedCallback = {messages ->
            CoroutineScope(Dispatchers.Main).launch {

                messages?.forEach {chatMessage ->
                    showLog(chatMessage.from.toString())
                    showLog(chatMessage.to.toString())
                    showLog(chatMessage.msgId.toString())
                    showLog("converationId:" + chatMessage.conversationId().toString())
                    val textMessageBody = chatMessage.body as TextMessageBody
                    showLog(textMessageBody.toString())

                    addItemToList(chatMessage)
                }
            }
        },
        onMessageStatusSuccessCallback = { chatMessage ->
            CoroutineScope(Dispatchers.Main).launch {

                // val remoteMessage = RemoteMessage.Builder(fcmDeviceToken_of_user2).build()
                //  FirebaseMessaging.getInstance().send(remoteMessage)
            }
        },
        onGetMessageListCompleteCallback = { list, fromPosition ->
            CoroutineScope(Dispatchers.Main).launch {
                if (fromPosition == "") {
                    messagesAdapter?.submitList(list)
                    progressDialog.dismissDialog()

                } else {
                    addListToAdappterList(list)
                }
            }
        },
        onModifyingMessage = {chatMessage ->
            updateItemInList(chatMessage)
        },
        onModifyingMessageError = {
            binding.root.showSnackbar(it.toString())
        }
    )




    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        agoraChatHelper.setUpChatClient(this)

        val type =  intent.getStringExtra("chat_type")
       chat_type = if(type == Conversation.ConversationType.Chat.toString()) Conversation.ConversationType.Chat else Conversation.ConversationType.GroupChat
       //When comes from User item
         userEntity =  intent.getParcelableExtra("user",UserEntity::class.java)
        //When comes from Group item.
       groupId = intent.getStringExtra("group_Id")
       group_Name =   intent.getStringExtra("group_Name")
       group_description =   intent.getStringExtra("group_description")
       group_owner =  intent.getStringExtra("group_owner")


        binding.contactName.text = if(chat_type == Conversation.ConversationType.Chat){
            binding.membersIcon.visibility = View.GONE
            userEntity?.nickname ?: userEntity?.username
        }else{
            binding.membersIcon.visibility = View.VISIBLE
            group_Name
        }

        lifecycleScope.launch {
            progressDialog.showDialog(supportFragmentManager)
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
                    sendMessage(messageStr)
                }
            }
        }
        setUpMessageRecyclerView()
        binding.membersIcon.setOnClickListener {
           lifecycleScope.launch {
               showMembersList(groupId.toString())
           }
        }
        binding.videoCall.setOnClickListener {
            if(chat_type == Conversation.ConversationType.Chat){

           //     val intent = Intent(this, VideoActivity::class.java)
             //   intent.putExtra("local_uid",userEntity?.username)
            //    startActivity(Intent(this, VideoActivity::class.java))
            }else{
              //  startActivity(Intent(this,GroupVideoCallActivity::class.java))
            }
        }
        binding.voiceCall.setOnClickListener {
            if(chat_type == Conversation.ConversationType.Chat){
             //   val intent = Intent(this,VoiceCallActivity::class.java)
               // intent.putExtra("name",userEntity?.username)
              //  intent.putExtra("chat_type",chat_type.toString())
               // startActivity(intent)



            }else{
                lifecycleScope.launch {
                    val crResult = agoraChatHelper.getGroupMembers(groupId.toString())
                    membersList = crResult?.data
               //     val intent = Intent(this@ChatActivity, VoiceCallActivity::class.java)
              //      intent.putExtra("name", group_Name)
//                    intent.putExtra("chat_type", chat_type.toString())
//                    intent.putExtra("membersList", membersList?.toTypedArray())
//                    startActivity(intent)
                }

            }
        }
    }
    private fun setUpMessageRecyclerView(){

        currentUser = agoraChatHelper.getCurrentUser().toString()
        messagesAdapter = MessagesAdapter(currentUser = currentUser , onLongClick = {chatMessage ->
            val dialogBuilder = MaterialAlertDialogBuilder(this)

            val binding = EditMsgDialogBinding.inflate(layoutInflater)
            dialogBuilder.setView(binding.root)
            val dialog = dialogBuilder.create()
            dialog.show()

            binding.doneEdit.setOnClickListener {
                val editedMsg = binding.editMsgEdittext.text.toString()
                agoraChatHelper.modifyChatMessage(chatMessage,editedMsg)
                dialog.cancel()
            }


        })

        binding.messagesRecyclerView.adapter = messagesAdapter
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true
        binding.messagesRecyclerView.layoutManager = linearLayoutManager
//        binding.MessagesRecyclerView.itemAnimator = null

        binding.messagesRecyclerView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->

            if(linearLayoutManager.findLastCompletelyVisibleItemPosition() == messagesAdapter?.currentList?.size?.minus(1)){
                if(messagesAdapter?.currentList?.isNotEmpty() == true){

                    binding.paginatingProgressBar.visibility = View.VISIBLE


                    lifecycleScope.launch {
                        if(chat_type == Conversation.ConversationType.Chat){
                            agoraChatHelper.getAsyncFetchConverationFromServer(userId = userEntity?.username.toString(),fromMessageId = messagesAdapter?.currentList?.last()?.msgId.toString(), chatType = Conversation.ConversationType.Chat)
                        }else{
                            agoraChatHelper.getAsyncFetchConverationFromServer(userId = groupId.toString(), fromMessageId = messagesAdapter?.currentList?.last()?.msgId.toString(), chatType = Conversation.ConversationType.GroupChat)

                        }
                    }
                }
            }

        }
    }


    fun addItemToList(chatMessage: ChatMessage?){

        val mList = messagesAdapter?.currentList?.toMutableList() as ArrayList
        mList.add(0,chatMessage)
        mList.forEach {
            Log.d("Fvlmkfv",it.toString())
        }
        messagesAdapter?.submitList(mList){
            binding.messagesRecyclerView.smoothScrollToPosition(0)
        }

    }

    fun addListToAdappterList(chatMessageList: List<ChatMessage>?){



        val oldSizePosition = messagesAdapter?.currentList?.size

        if(!chatMessageList.isNullOrEmpty()){
            val mList = messagesAdapter?.currentList?.toMutableList()
            chatMessageList.forEach {
                mList?.add(it)
            }
            messagesAdapter?.submitList(mList){
                /** Refreshing RecyclerView with SmoothScroll of LineaerLayoutManager to scroll to new list and last item of old list will also be visible with help of offSet in pixels.
                 */
                (binding.messagesRecyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(oldSizePosition?.minus(-1)!!,170)
                binding.paginatingProgressBar.visibility = View.GONE
            }
        }else{
            binding.paginatingProgressBar.visibility = View.GONE
        }
    }

    fun updateItemInList(chatMessage: ChatMessage?){
        val list =  messagesAdapter?.currentList?.map {
            if(it.msgId == chatMessage?.msgId){
                chatMessage
            }else{
                it
            }
        }
        messagesAdapter?.submitList(list)
    }

    fun sendMessage(messageStr : String){
        invalidateViewOnSeendMessageClick()
        CoroutineScope(Dispatchers.IO).launch {
            if(chat_type == Conversation.ConversationType.Chat){
                agoraChatHelper.sendMessage(message = messageStr, toUserId = userEntity?.username.toString(), chatType = ChatMessage.ChatType.Chat, onCreateMessage = {
                    CoroutineScope(Dispatchers.Main).launch {
                        addItemToList(it)
                    }

                })

            }
            else{
                agoraChatHelper.sendMessage(message = messageStr, toUserId = groupId.toString(), chatType = ChatMessage.ChatType.GroupChat , onCreateMessage = {
                    CoroutineScope(Dispatchers.Main).launch {
                        addItemToList(it)
                    }
                })
            }
        } }
    fun invalidateViewOnSeendMessageClick(){
        binding.messageInputEditText.text = null
        val inoutMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inoutMethodManager.hideSoftInputFromWindow(binding.root.windowToken,0)
    }

    @SuppressLint("SuspiciousIndentation")
    suspend fun showMembersList(groupId : String){

       val crResult = agoraChatHelper?.getGroupMembers(groupId)
       val groupJoinedMembersList = crResult?.data

        val dialogBuilder = MaterialAlertDialogBuilder(this)
        dialogBuilder.setItems(groupJoinedMembersList?.toTypedArray(),object  : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {

                }
            })
        dialogBuilder.show()
    }

}
package com.example.chat__groupchatapp

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.example.chat__groupchatapp.Utils.showLog
import io.agora.CallBack
import io.agora.ConnectionListener
import io.agora.GroupChangeListener
import io.agora.MessageListener
import io.agora.ValueCallBack
import io.agora.chat.ChatClient
import io.agora.chat.ChatMessage
import io.agora.chat.ChatMessage.ChatType
import io.agora.chat.ChatOptions
import io.agora.chat.Conversation
import io.agora.chat.CursorResult
import io.agora.chat.FetchMessageOption
import io.agora.chat.Group
import io.agora.chat.GroupManager.GroupStyle
import io.agora.chat.GroupOptions
import io.agora.chat.MessageBody
import io.agora.chat.TextMessageBody
import io.agora.push.PushConfig
import io.agora.push.PushHelper
import io.agora.push.PushListener
import io.agora.push.PushType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AgoraChatHelper(
    val onMessageRecievedCallback : (MutableList<ChatMessage>?) -> Unit = {},
    val onConnectedCallback : () -> Unit = {},
    val onDisconnectedCallback : (errorCode: Int) -> Unit = {},
    val onLogoutListenerCallback : (errorCode: Int, info: String?) -> Unit = {errorCode,info ->},
    val onTokenExpiredCallback : () -> Unit = {},
    val onTokenWillExpireCallback : () -> Unit = {},
    val onLoginCallback : () -> Unit = {},
    val onLoginFailureCallback : (Int, String?) -> Unit = {i,s ->},
    val onLogoutCallback : () -> Unit = {},
    val onLogoutFailureCallback : (Int, String?) -> Unit ={i,s->},
    val onMessageStatusSuccessCallback : (sentMessage :  ChatMessage?) -> Unit = {},
    val onMessageStatusFailureCallback : (Int, String?) -> Unit = {i,s->},
    val onGetMessageListCompleteCallback : (MutableList<ChatMessage>, String) -> Unit = {i,s->},
    val onErrorr : (String?) -> Unit={},
    val onModifyingMessage : (ChatMessage?) -> Unit = {},
    val onModifyingMessageError : (String?) -> Unit = {}, ) {

    private var chatClient : ChatClient? = null
    var isJoined = false

    private var fcm_senderId = "838403807403"


    fun setUpChatClient(context : Context){
        try {
            val chatOptions = ChatOptions()
            chatOptions.appKey = context.getString(R.string.APP_KEY)

            val pushConfigBuilder = PushConfig.Builder(context)
            //  val pushConfig = pushConfigBuilder.enableFCM(fcm_senderId).build()

            //    chatOptions.pushConfig = pushConfig

            chatClient = ChatClient.getInstance()
            chatClient?.init(context, chatOptions)


        }catch (e:Exception){
            onErrorr(e.message)
        }
       /*  PushHelper.getInstance().setPushListener(object : PushListener(){
            override fun onError(pushType: PushType?, errorCode: Long) {
                showLog("Push Client error" +  "  ${pushType}")
            }

            override fun isSupportPush(pushType: PushType?, pushConfig: PushConfig?): Boolean {
                showLog(pushConfig?.fcmSenderId.toString() + "  isSupportPush")
                return if(pushType == PushType.FCM){
                   // GoogleApiAvailabilityLight.getInstance().isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS
                }else{
                    super.isSupportPush(pushType, pushConfig)
                }
            }
        }) */

      //  sendFcmToken_toChatServer(context)



    }



/*     fun sendFcmToken_toChatServer(context: Context){
         *//*  if(GoogleApiAvailabilityLight.getInstance().isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS)
         {
         return
         } *//*
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if(it.isSuccessful){
                chatClient?.sendFCMTokenToServer(it.result)

                showLog("Fcm token ${it.result}")
            }else{
                showLog("Fcm token fetching failed")
            }
        }

    } */

    fun setUpChatListeners(){
        chatClient?.chatManager()?.addMessageListener(messageListener)
        chatClient?.addConnectionListener(connectionListener)
    }

    val messageListener = object : MessageListener {
        override fun onMessageReceived(messages: MutableList<ChatMessage>?) {
            showLog("onMessageReceived")
            onMessageRecievedCallback(messages)

        }
    }
    val connectionListener = object : ConnectionListener {
        override fun onConnected() {
            showLog("onConnected")
            onConnectedCallback()
        }

        override fun onDisconnected(errorCode: Int) {
            showLog(errorCode.toString())
            showLog("onDisconnected")
            onDisconnectedCallback(errorCode)
        }

        override fun onLogout(errorCode: Int, info: String?) {
            super.onLogout(errorCode, info)
            showLog("onLogout")
            onLogoutListenerCallback(errorCode, info)
        }

        override fun onTokenExpired() {
            super.onTokenExpired()
            showLog("onTokenExpired")
            onTokenExpiredCallback()
        }

        override fun onTokenWillExpire() {
            super.onTokenWillExpire()
            showLog("onTokenWillExpire")
            onTokenWillExpireCallback()
        }
    }


    fun removeMessageListener(){
        chatClient?.chatManager()?.removeMessageListener(messageListener)
    }
    fun removeConnectionListener(){
        chatClient?.removeConnectionListener(connectionListener)
    }

    fun loginToChat(userId : String, pwd : String){
        chatClient?.login(userId,pwd,object : CallBack {
            override fun onSuccess() {
                showLog("Login  success")
                isJoined = true
                onLoginCallback()
            }

            override fun onError(code: Int, error: String?) {
                showLog("Login  onError")
                showLog("Login  " + code.toString())
                showLog("Login  " + error.toString())
                onLoginFailureCallback(code, error)
            }
        })
    }
    fun logout( onLogoutSuccess : () -> Unit = {}, onLogoutError : (String?) -> Unit = {}){
        chatClient?.logout(true,object: CallBack{
            override fun onSuccess() {
                showLog("logout success")
                isJoined = false

                onLogoutSuccess()
            }

            override fun onError(code: Int, error: String?) {
                showLog("logout onError")
                showLog("logout"  + code.toString())
                showLog("logout" + error.toString())
                onLogoutError(error)
            }
        })
    }

    fun getCurrentUser() = chatClient?.currentUser
    fun isUserLoggedIn() = chatClient?.isLoggedInBefore

    fun sendMessage(message: String?, toUserId : String, onCreateMessage : (ChatMessage) -> Unit, chatType : ChatType) {

        Log.d("dkfnjkdvnd",message.toString())
        if(message == null || message == "null" || message.isNullOrEmpty()){
            onErrorr("Message is empty.")
            return
        }

        if(toUserId.isNullOrEmpty()){
            onErrorr("UserId should not empty")
            showLog("UserId should not empty")
            return
        }

        val chatMessage = ChatMessage.createTextSendMessage(message,toUserId)
            chatMessage.chatType = chatType

        onCreateMessage(chatMessage)

        chatMessage.setMessageStatusCallback(object : CallBack{
            override fun onSuccess() {
                showLog("Message Sent Successfully")
                onMessageStatusSuccessCallback(chatMessage)
            }

            override fun onError(code: Int, error: String?) {
                showLog("Message gets Error")
                onMessageStatusFailureCallback(code,error)
            }
        })


        chatClient?.chatManager()?.sendMessage(chatMessage)
    }

    @SuppressLint("SuspiciousIndentation")
    suspend fun getAsyncFetchConverationFromServer(userId : String, fromMessageId : String = "", chatType : Conversation.ConversationType) = withContext(Dispatchers.IO){


        var list = mutableListOf <ChatMessage>()
        Log.d("Fvfnvfv",userId.toString())
        //We need to paas converation Id of another User
        //example if we are user1 and are talking to user2 then we need to paas converationId of user2
        chatClient?.chatManager()?.asyncFetchHistoryMessages(userId.lowercase(),chatType,20,fromMessageId, null , object : ValueCallBack<CursorResult<ChatMessage>> {
            override fun onSuccess(value: CursorResult<ChatMessage>?) {
                CoroutineScope(Dispatchers.Main).launch {
                    Log.d("fbkmkbm", "onSuccess History messages")
                    Log.d("vlmkbmf",value?.data?.size.toString() + "  ecs")
                    value?.data?.forEach {
                        list.add(it)

                    }


                    onGetMessageListCompleteCallback(list, fromMessageId)

                    //Below code for testing pagination.
                    /*   value?.data?.size?.let{ size ->
                          if( size > 0){
                           //   getAsyncFetchConverationFromServer()
                          }else{

                              onGetMessageListCompleteCallback(list)
                          }

                      } */
                }

            }

            override fun onError(error: Int, errorMsg: String?) {
                Log.d("Blgmbkgb",errorMsg.toString())
                onErrorr(errorMsg)
            }
        })
    }

    fun getMessagesCount(){
        Log.d("fvbkfnbnbkfbfg",chatClient?.chatManager()?.allConversations?.get("user2")?.lastMessage.toString())
    }

    fun modifyChatMessage(chatMessage: ChatMessage, editedMessage : String){

        if(chatMessage.body is TextMessageBody){
            val messageBody = TextMessageBody(editedMessage)

            chatClient?.chatManager()?.asyncModifyMessage(chatMessage.msgId, messageBody,object : ValueCallBack<ChatMessage>{
                override fun onSuccess(chatMessage: ChatMessage?) {
                    Log.d("fblmfkb",chatMessage.toString())
                    onModifyingMessage(chatMessage)



                }

                override fun onError(errorCode : Int, errorMsg: String?) {
                    Log.d("fkvnjnvkfv",errorMsg.toString())
                    onModifyingMessageError(errorMsg)
                }

                override fun onProgress(progress: Int, status: String?) {
                    Log.d("fbmkbf",status.toString())
                    super.onProgress(progress, status)
                }
            })
        }else{
        }
    }

    suspend fun getJoinedGroups(): MutableList<Group>? = withContext(Dispatchers.IO){
        chatClient?.groupManager()?.loadAllGroups()
       return@withContext chatClient?.groupManager()?.joinedGroupsFromServer

    }

    fun createChatGroup(groupName : String, groupDesc : String, allMembers : Array<String>){

        try {
            val groupOptions = GroupOptions().apply {
                maxUsers = 100
                style = GroupStyle.GroupStylePrivateMemberCanInvite
            }
            chatClient?.groupManager()
                ?.createGroup(groupName, groupDesc, allMembers, "No Reason", groupOptions,)
        }catch (e:Exception){
            showLog(e.message + " create Group error.")
        }
    }

    fun setGroupChangeListener(groupChangeListener: GroupChangeListener){
        chatClient?.groupManager()?.addGroupChangeListener(groupChangeListener)
    }

    fun removeGroupChangeListener(groupChangeListener: GroupChangeListener){
        chatClient?.groupManager()?.removeGroupChangeListener(groupChangeListener)
    }

}
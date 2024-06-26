package com.example.chat__groupchatapp.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.chat__groupchatapp.AgoraUiKitUtils.PermissionsManager
import com.example.chat__groupchatapp.R
import com.example.chat__groupchatapp.data.remote.model.user.response.UserEntity
import com.example.chat__groupchatapp.databinding.ActivityAgoraChatUiactivityBinding
import com.example.chat__groupchatapp.ui.fagments.CustomEaseChatFragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailabilityLight
import com.google.firebase.messaging.FirebaseMessaging
import io.agora.CallBack
import io.agora.MessageListener
import io.agora.ValueCallBack
import io.agora.chat.ChatClient
import io.agora.chat.ChatMessage
import io.agora.chat.ChatOptions
import io.agora.chat.Conversation
import io.agora.chat.CursorResult
import io.agora.chat.TextMessageBody
/*import io.agora.chat.callkit.EaseCallKit
import io.agora.chat.callkit.bean.EaseCallUserInfo
import io.agora.chat.callkit.general.EaseCallEndReason
import io.agora.chat.callkit.general.EaseCallError
import io.agora.chat.callkit.general.EaseCallKitConfig
import io.agora.chat.callkit.general.EaseCallType
import io.agora.chat.callkit.listener.EaseCallGetUserAccountCallback
import io.agora.chat.callkit.listener.EaseCallKitListener
import io.agora.chat.callkit.listener.EaseCallKitTokenCallback
import io.agora.chat.callkit.utils.EaseCallMsgUtils*/
import io.agora.chat.uikit.EaseUIKit
import io.agora.chat.uikit.chat.EaseChatFragment
import io.agora.chat.uikit.chat.interfaces.OnMessageSendCallBack
import io.agora.chat.uikit.menu.EaseChatType
import io.agora.push.PushConfig
import io.agora.push.PushHelper
import io.agora.push.PushListener
import io.agora.push.PushType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject


class AgoraChatUIActivity : AppCompatActivity() {

    private var channel: String? = null
    private var group_Name: String? = null
    private var groupId: String?= ""
    private var group_description: String?= null
    private var group_owner: String?= null
    private var userEntity: UserEntity?= null
    private var chat_type: Conversation.ConversationType? = null
    lateinit var binding : ActivityAgoraChatUiactivityBinding




   // lateinit var  easeCallKit : EaseCallKit
    lateinit var  easeUIKit : EaseUIKit

    var fcmSenderId  = "264683372480"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgoraChatUiactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)




      //  easeCallKit = EaseCallKit.getInstance()
        easeUIKit =  EaseUIKit.getInstance()



        val type =  intent.getStringExtra("chat_type")
        chat_type = if(type == Conversation.ConversationType.Chat.toString()) Conversation.ConversationType.Chat else Conversation.ConversationType.GroupChat
        //When comes from User item
        userEntity = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
              intent.getParcelableExtra("user", UserEntity::class.java)
        }else{
            intent.getParcelableExtra<UserEntity>("user")
        }

        //When comes from Group item.
        groupId = intent.getStringExtra("group_Id")
        group_Name =   intent.getStringExtra("group_Name")
        group_description =   intent.getStringExtra("group_description")
        group_owner =  intent.getStringExtra("group_owner")

        if(chat_type == Conversation.ConversationType.Chat){
            startChat(userEntity?.username, EaseChatType.SINGLE_CHAT)
        }else if(chat_type == Conversation.ConversationType.GroupChat){
            startChat(groupId, EaseChatType.GROUP_CHAT)
        }

        initChatUISDK()
        // initAgoraCallKitSdk()
        initFcmWithChatClient()
       // Log.d("fvnbknkgjbgb",PushHelper.getInstance().pushToken.toString())
        PushHelper.getInstance().register()
        fetchGroupMembers()

        PushHelper.getInstance().setPushListener(object : PushListener(){
            override fun onError(pushType: PushType?, errorCode: Long) {
                Log.d("vkfnvkfnv","error" + errorCode.toString() + pushType?.name.toString())
            }

            override fun isSupportPush(pushType: PushType?, pushConfig: PushConfig?): Boolean {
                if(pushType == PushType.FCM){
                    Log.d("bkmgkbnmg","yes")
                    return GoogleApiAvailabilityLight.getInstance().isGooglePlayServicesAvailable(this@AgoraChatUIActivity) == ConnectionResult.SUCCESS
                }else{
                    Log.d("bkmgkbnmg","no")

                }
                return super.isSupportPush(pushType, pushConfig)
            }
        })

        binding.videoCallButton.setOnClickListener {

            if(chat_type == Conversation.ConversationType.Chat){
                val channelName = System.currentTimeMillis().toString()

                val intent = Intent(this, VideoCallActivity::class.java)
                intent.putExtra(MConstants.IS_INCOMING_CALL, "false")
                intent.putExtra(MConstants.TARGET_USER_ID, userEntity?.username)
                intent.putExtra(MConstants.CHANNEL_NAME, channelName)
                intent.putExtra(MConstants.NICKNAME,userEntity?.nickname ?: "")
                intent.putExtra(MConstants.CALL_TYPE,MConstants.SINGLE_CALL_TYPE_VALUE)
                intent.putExtra(MConstants.VOICE_OR_VIDEO,MConstants.VIDEO_CALL_VALUE)
                intent.putExtra(MConstants.CALLER_ID,ChatClient.getInstance().currentUser)
                intent.putExtra(MConstants.CALL_OR_CHAT, MConstants.CALL_VALUE)
                startActivity(intent)

             //   sendCallMessage(callType = MConstants.SINGLE_CALL_TYPE_VALUE, voice_or_video =  MConstants.VIDEO_CALL_VALUE,to = userEntity?.username.toString())

                /*   val ext = hashMapOf<String,Any>()
                   ext.put(EaseCallMsgUtils.CALL_CHANNELNAME, "CHANNEL_SINGLE_CALLLE")
                   easeCallKit.startSingleCall(EaseCallType.SINGLE_VIDEO_CALL,userEntity?.username,ext,CallSingleBaseActivity::class.java)
   */
            }
            else{

                val channelName = System.currentTimeMillis().toString()

                val intent = Intent(this, GroupVideoCallActivity::class.java)
                intent.putExtra(MConstants.IS_INCOMING_CALL, "false")
                intent.putExtra(MConstants.TARGET_USER_ID, userEntity?.username)
                intent.putExtra(MConstants.CHANNEL_NAME, channelName)
                intent.putExtra(MConstants.NICKNAME,userEntity?.nickname ?: "")
                intent.putExtra(MConstants.CALL_TYPE,MConstants.GROUP_CALL_TYPE_VALUE)
                intent.putExtra(MConstants.VOICE_OR_VIDEO,MConstants.VIDEO_CALL_VALUE)
                intent.putExtra(MConstants.CALLER_ID,ChatClient.getInstance().currentUser)
                intent.putExtra(MConstants.CALL_OR_CHAT, MConstants.CALL_VALUE)
                intent.putExtra(MConstants.GROUP_ID,groupId)
                intent.putExtra(MConstants.GROUP_NAME,group_Name)
                intent.putExtra(MConstants.GROUP_OWNER,group_owner)
                intent.putExtra(MConstants.GROUP_DESCRIPTION,group_description)
                intent.putStringArrayListExtra(MConstants.GROUP_MEMBERS_LIST,membersList)

                startActivity(intent)

              //  sendCallMessage(callType = MConstants.GROUP_CALL_TYPE_VALUE, voice_or_video = MConstants.VIDEO_CALL_VALUE,to = userEntity?.username.toString())

                    val membersList = ChatClient.getInstance().groupManager().asyncFetchGroupMembers(groupId, "", 100,object : CallBack,
                        ValueCallBack<CursorResult<String>> {
                        override fun onSuccess() {
                        }

                        override fun onSuccess(value: CursorResult<String>?) {

                            /*runOnUiThread {
                                val map = hashMapOf<String,String>()

                                val ext: HashMap<String, Any> = HashMap()
                                ext.put("groupId", groupId.toString());
                                ext.put(EaseCallMsgUtils.CALL_CHANNELNAME, "CHANNEL_GROUP_CALLLE");


                                easeCallKit.startInviteMultipleCall(EaseCallType.CONFERENCE_VIDEO_CALL,null, ext,CallMultipleBaseActivity::class.java)
                                    sendChatMessage("Video")

                            }*/


                        }

                        override fun onError(code: Int, error: String?) {
                            TODO("Not yet implemented")
                        }

                        override fun onProgress(progress: Int, status: String?) {
                        }
                    })

                }
        }


        binding.callButton.setOnClickListener {
            val userInfoMap2: MutableMap<String, Any> = HashMap()
            userInfoMap2["name"] = userEntity?.username.toString()
            userInfoMap2["name1"] = userEntity?.username.toString()


            val pushObject = JSONObject()
            try{
                //When app is in forground then set title to default title key of Agora (em_push_title)

                //When app is in forground then set content to default content key of Agora (em_push_content)
                //This is custom key added in data object.
               // pushObject.put("chatType",EaseCallType.SINGLE_VIDEO_CALL)
               //  pushObject.put("channel",easeCallKit.channelName)

                /*   pushObject.put("alert",message?.body?: " ALERT EMPTY")
                  titleArgs.put("value1")
                  pushObject.put("Title", titleArgs)
                  contentArgs.put("value1")
                  pushObject.put("Content",contentArgs) */
            }catch (e:Exception){
                Log.d("Fknbkjbg",e.message.toString())
            }

            //  message?.setAttribute("em_push_template",pushObject)
            //This em_apns_ext key is used for pushNotification ext field.
            val hashMap = HashMap<String,Any>()
            hashMap.put("em_apns_ext",pushObject)
            //  hashMap.put("em_force_notification",pushObject)


            if(chat_type == Conversation.ConversationType.Chat) {

                val channelName = System.currentTimeMillis().toString()

                val intent = Intent(this, VoiceCallActivity::class.java)
                intent.putExtra(MConstants.IS_INCOMING_CALL, "false")
                intent.putExtra(MConstants.TARGET_USER_ID, userEntity?.username)
                intent.putExtra(MConstants.CHANNEL_NAME, channelName)
                intent.putExtra(MConstants.NICKNAME,userEntity?.nickname ?: "")
                intent.putExtra(MConstants.CALL_TYPE,MConstants.SINGLE_CALL_TYPE_VALUE)
                intent.putExtra(MConstants.VOICE_OR_VIDEO,MConstants.VOICE_CALL_VALUE)
                intent.putExtra(MConstants.CALLER_ID,ChatClient.getInstance().currentUser)
                intent.putExtra(MConstants.CALL_OR_CHAT, MConstants.CALL_VALUE)
                startActivity(intent)


             //   sendCallMessage(callType = MConstants.SINGLE_CALL_TYPE_VALUE, voice_or_video =  MConstants.VOICE_CALL_VALUE, to = userEntity?.username.toString())

                //easeCallKit.startSingleCall(EaseCallType.SINGLE_VOICE_CALL, userEntity?.username, null, CallSingleBaseActivity::class.java)
            }else{
                val channelName = System.currentTimeMillis().toString()


                val intent = Intent(this, VoiceCallActivity::class.java)
                intent.putExtra(MConstants.IS_INCOMING_CALL, "false")
                intent.putExtra(MConstants.TARGET_USER_ID, userEntity?.username)
                intent.putExtra(MConstants.CHANNEL_NAME, channelName)
                intent.putExtra(MConstants.NICKNAME,userEntity?.nickname ?: "")
                intent.putExtra(MConstants.CALL_TYPE,MConstants.GROUP_CALL_TYPE_VALUE)
                intent.putExtra(MConstants.VOICE_OR_VIDEO,MConstants.VOICE_CALL_VALUE)
                intent.putExtra(MConstants.CALLER_ID,ChatClient.getInstance().currentUser)
                intent.putExtra(MConstants.CALL_OR_CHAT, MConstants.CALL_VALUE)
                intent.putExtra(MConstants.GROUP_ID,groupId)
                intent.putExtra(MConstants.GROUP_NAME,group_Name)
                intent.putExtra(MConstants.GROUP_OWNER,group_owner)
                intent.putExtra(MConstants.GROUP_DESCRIPTION,group_description)
                intent.putStringArrayListExtra(MConstants.GROUP_MEMBERS_LIST,membersList)

                startActivity(intent)

             //   sendCallMessage(callType = MConstants.GROUP_CALL_TYPE_VALUE, voice_or_video = MConstants.VOICE_CALL_VALUE, to = userEntity?.username.toString())
             }
        }

        //easeCallKit.setCallKitListener(easeCallKitListener)

    }



    fun initChatUISDK(){
        val  chatoptions = ChatOptions()
        chatoptions.appKey = getString(R.string.APP_KEY)
        chatoptions.requireDeliveryAck = true
        chatoptions.autoLogin = true
        easeUIKit.init(this,chatoptions)

        ChatClient.getInstance().chatManager().addMessageListener(object :MessageListener{
            override fun onMessageReceived(messages: MutableList<ChatMessage>?) {

            }

            override fun onCmdMessageReceived(messages: MutableList<ChatMessage>?) {
                super.onCmdMessageReceived(messages)
            }


        })
    }

    fun startChat(user : String?, easeChatType : EaseChatType){

        val easeChatFragmentBuilder = EaseChatFragment.Builder(user,easeChatType)
        easeChatFragmentBuilder.useHeader(false)

        easeChatFragmentBuilder.setOnChatExtendMenuItemClickListener { view, itemId ->
            if(itemId == io.agora.chat.uikit.R.id.extend_item_take_picture) {
                return@setOnChatExtendMenuItemClickListener !checkPermissions(Manifest.permission.CAMERA, 111);
            }else if(itemId == io.agora.chat.uikit.R.id.extend_item_picture || itemId == io.agora.chat.uikit.R.id.extend_item_file || itemId == io.agora.chat.uikit.R.id.extend_item_video) {
                return@setOnChatExtendMenuItemClickListener !checkPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, 112);
            }
            return@setOnChatExtendMenuItemClickListener false;
        }
            .setOnChatRecordTouchListener { v, event ->
                return@setOnChatRecordTouchListener !checkPermissions(Manifest.permission.RECORD_AUDIO, 113);
            }
            .setOnMessageSendCallBack(object : OnMessageSendCallBack{
                override fun onSuccess(message: ChatMessage?) {
                    super.onSuccess(message)
              Log.d("Fvfkvfmm","message Sent succefully")  }
                override fun onError(code: Int, errorMsg: String?) {
                    Log.d("Fvfkvfmm","message error")
            }
            })
            .showNickname(true)
        easeChatFragmentBuilder.setCustomFragment(CustomEaseChatFragment())


        val easeChatFragment = easeChatFragmentBuilder.build()
        supportFragmentManager.beginTransaction().replace(binding.flFragment.id,easeChatFragment).commit()

    }

    private fun checkPermissions(permission: String, requestCode: Int): Boolean {
        if (!PermissionsManager.getInstance().hasPermission(this, permission)) {
            PermissionsManager.getInstance()
                .requestPermissions(this, arrayOf<String>(permission), requestCode)
            return false
        }
        return true
    }

/*
    private fun initAgoraCallKitSdk(){
        val easeCallKitConfig = EaseCallKitConfig()
            easeCallKitConfig.callTimeOut = 15
        easeCallKitConfig.agoraAppId = getString(R.string.APP_ID)
        easeCallKitConfig.isEnableRTCToken = true

        val userInfoMap: MutableMap<String, EaseCallUserInfo> = HashMap()
        userInfoMap["***"] = EaseCallUserInfo("***", null)
        userInfoMap["***"] = EaseCallUserInfo("****", null)
        easeCallKitConfig.setUserInfoMap(userInfoMap)

       easeCallKit.init(applicationContext,easeCallKitConfig)

       easeCallKit.registerVideoCallClass(CallSingleBaseActivity::class.java)
       easeCallKit.registerMultipleVideoClass(CallMultipleBaseActivity::class.java)
    }
*/

/*
    val easeCallKitListener = object : EaseCallKitListener{
        override fun onInviteUsers(
            callType: EaseCallType?,
            existMembers: Array<out String>?,
            ext: JSONObject?,
        ) {
            Log.d("fvmkf3232mv","onInviteIsers")
        }

        override fun onEndCallWithReason(
            callType: EaseCallType?,
            channelName: String?,
            reason: EaseCallEndReason?,
            callTime: Long,
        ) {
            Log.d("fvmkf3232mv","onEndCallWithReason")
        }

        override fun onReceivedCall(
            callType: EaseCallType?,
            fromUserId: String?,
            ext: JSONObject?,
        ) {
            Log.d("fvmkf3232mv","onReceivedCall")
        }

        override fun onGenerateRTCToken(
            userId: String?,
            channelName: String?,
            callback: EaseCallKitTokenCallback?,
        ) {
            super.onGenerateRTCToken(userId, channelName, callback)

            channel = channelName

            val token = TokenBuilder.getRtcTokenOfUid(this@AgoraChatUIActivity,userId?.toInt() ?: 0,channelName.toString(),RtcTokenBuilder2.Role.ROLE_PUBLISHER)

            callback?.onSetToken(token, userId?.toInt()?: 0)
        }

        override fun onCallError(type: EaseCallError?, errorCode: Int, description: String?) {

            Log.d("fbfmbkfbmfg",type?.name.toString())
            Log.d("fbfmbkfbmfg",type.toString())
            Log.d("fbfmbkfbmfg",description.toString())
            Log.d("fbfmbkfbmfg",errorCode.toString())
            Log.d("fvmkf3232mv","onCallError")
        }

        override fun onInViteCallMessageSent() {
            Log.d("fvmkf3232mv","onInViteCallMessageSent")
            Log.d("fkbmkbmf",channel.toString())
           // sendChatMessage()
        }

        override fun onRemoteUserJoinChannel(
            channelName: String?,
            userName: String?,
            uid: Int,
            callback: EaseCallGetUserAccountCallback?,
        ) {
            Log.d("fvkmkbff",channelName.toString())
            Log.d("fvmkf3232mv","onRemoteUserJoinChannel")
        }

        override fun onUserInfoUpdate(userName: String?) {
            super.onUserInfoUpdate(userName)
        }
    }
*/

    @SuppressLint("SuspiciousIndentation")
    private fun initFcmWithChatClient(){

        val chatOptions = ChatOptions()
        chatOptions.appKey = getString(R.string.APP_KEY)

        val pushConfigBuilder =  PushConfig.Builder(applicationContext)
        pushConfigBuilder.enableFCM(fcmSenderId)

        chatOptions.pushConfig = pushConfigBuilder.build()

        ChatClient.getInstance().init(applicationContext,chatOptions)


      val isAvaialble =   GoogleApiAvailabilityLight.getInstance().isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS
        Log.d("fvknvjfnv",isAvaialble.toString())

        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if(it.isSuccessful){
                Log.d("fbkgmnmnbkgmng",it.result.toString())
                ChatClient.getInstance().sendFCMTokenToServer(it.result)



                ChatClient.getInstance().pushManager().bindDeviceToken(fcmSenderId,it.result,object : CallBack{
                    override fun onSuccess() {
                       Log.d("gfkbngjbng","succes")
                    }

                    override fun onError(code: Int, error: String?) {
                        Log.d("gfkbngjbng","succes")
                    }

                    override fun onProgress(progress: Int, status: String?) {
                        super.onProgress(progress, status)
                        Log.d("gfkbngjbng","succes")
                    }
                })
            }else{
                Log.d("fvknfkvnfv",it.exception?.message.toString() + " error")
            }
        }

    }

    fun sendCallMessage(callType : String, voice_or_video : String, to : String){
        val chatMessage = ChatMessage.createSendMessage(ChatMessage.Type.TXT)
        val textMessage = TextMessageBody("action1")
       val channelName =  System.currentTimeMillis().toString() + userEntity?.username.toString()
        val pushObject = JSONObject()
        val titleArgs = JSONArray()
        val contentArgs = JSONArray()

        try{
            pushObject.put(MConstants.IS_INCOMING_CALL,"true")
            pushObject.put(MConstants.CHANNEL_NAME, channelName)
            pushObject.put(MConstants.TARGET_USER_ID,userEntity?.username)
            pushObject.put(MConstants.CALL_TYPE,callType)
            pushObject.put(MConstants.VOICE_OR_VIDEO,voice_or_video)
            pushObject.put(MConstants.CALLER_ID,ChatClient.getInstance().currentUser)
            pushObject.put(MConstants.CALL_OR_CHAT, "Call")

        }catch (e:Exception){
            Log.d("Fknbkjbg",e.message.toString())
        }

        Log.d("fvlmkvmfv",userEntity?.username.toString())
        chatMessage.to = userEntity?.username
        // chatMessage.to = user

        chatMessage.chatType = ChatMessage.ChatType.Chat
        chatMessage.body = textMessage

        chatMessage?.setAttribute("em_apns_ext",pushObject)
      //  chatMessage?.setAttribute("em_force_notification", true);

        chatMessage.setMessageStatusCallback(object: CallBack{
            override fun onSuccess() {

                Log.d("gbkngkbngbg","Chat Message send succefully")
            }

            override fun onError(code: Int, error: String?) {
                Log.d("fvkfvkfv",error.toString())
                Log.d("gbkngkbngbg","Chat Message onError ")
            }

            override fun onProgress(progress: Int, status: String?) {
                super.onProgress(progress, status)
                Log.d("gbkngkbngbg","onProgress")
            }
        })

        ChatClient.getInstance().chatManager().sendMessage(chatMessage)

    }

    fun sendChatMessage(type: String) {
        val chatMessage = ChatMessage.createSendMessage(ChatMessage.Type.TXT)
        val textMessage = TextMessageBody("message content")

        val pushObject = JSONObject()
        val titleArgs = JSONArray()
        val contentArgs = JSONArray()

        //If dont want to push notification to message chatType only then paas "null" to to (to is username).
        //  message?.to = "null"
        try{
            //This is custom key added in data object.
            val currentUser = ChatClient.getInstance().currentUser.toString()
            pushObject.put("alert","Incoming $type Call from group: $group_Name")
            //When app is in forground then set title to default title key of Agora (em_push_title)
            //When app is in forground then set content to default content key of Agora (em_push_content)
            //This is custom key added in data object.
            pushObject.put("chatType",type)
            pushObject.put("channel",channel)
            /*   pushObject.put("alert",message?.body?: " ALERT EMPTY")
              titleArgs.put("value1")
              pushObject.put("Title", titleArgs)
              contentArgs.put("value1")
              pushObject.put("Content",contentArgs) */
        }catch (e:Exception){
            Log.d("Fknbkjbg",e.message.toString())
        }

        //  message?.setAttribute("em_push_template",pushObject)
        //This em_apns_ext key is used for pushNotification ext field.

        chatMessage.to = groupId
       // chatMessage.to = user

        chatMessage.chatType = ChatMessage.ChatType.GroupChat
        chatMessage.body = textMessage

        chatMessage?.setAttribute("em_apns_ext",pushObject)
        chatMessage?.setAttribute("em_force_notification", true);

        chatMessage.setMessageStatusCallback(object: CallBack{
            override fun onSuccess() {
                Log.d("gbkngkbngbg","Chat Message send succefully")
            }

            override fun onError(code: Int, error: String?) {
                Log.d("fvkfvkfv",error.toString())
                Log.d("gbkngkbngbg","Chat Message onError ")
            }

            override fun onProgress(progress: Int, status: String?) {
                super.onProgress(progress, status)
                Log.d("gbkngkbngbg","onProgress")
            }
        })

        ChatClient.getInstance().chatManager().sendMessage(chatMessage)

    }


    fun sendCmdMessage(userId : String) {

        val cmdMsg = ChatMessage.createSendMessage(ChatMessage.Type.TXT)

        Log.d("ifnvivfnvjfkvkf",cmdMsg?.body.toString() + "  empty")

        val pushObject = JSONObject()
        val titleArgs = JSONArray()
        val contentArgs = JSONArray()

        //If dont want to push notification to message chatType only then paas "null" to to (to is username).
        cmdMsg?.to = userId


        try{
            //This is custom key added in data object.
            pushObject.put("alert",cmdMsg?.body)
            //When app is in forground then set title to default title key of Agora (em_push_title)
            pushObject.put("em_push_title",cmdMsg?.from)
            //When app is in forground then set content to default content key of Agora (em_push_content)
            pushObject.put("em_push_content",cmdMsg?.body)
            //This is custom key added in data object.
            pushObject.put("chatType",cmdMsg?.chatType.toString())
            /*   pushObject.put("alert",message?.body?: " ALERT EMPTY")
              titleArgs.put("value1")
              pushObject.put("Title", titleArgs)
              contentArgs.put("value1")
              pushObject.put("Content",contentArgs) */
        }catch (e:Exception){
            Log.d("Fknbkjbg",e.message.toString())
        }

        //  message?.setAttribute("em_push_template",pushObject)
        //This em_apns_ext key is used for pushNotification ext field.
        cmdMsg?.setAttribute("em_apns_ext",pushObject)
        cmdMsg?.setAttribute("em_force_notification", true);


// Sets the chat type as one-to-one chat, group chat, or chat room.
// Sets the chat type as one-to-one chat, group chat, or chat room.
      //  cmdMsg.chatType = ChatType.GroupChat
       // val action = "action1"
// You can customize the action.
// You can customize the action.
      //  val cmdBody = CmdMessageBody(action)
// Sets the message recipient: user ID of the recipient for one-to-one chat, group ID for group chat, or chat room ID for a chat room.
// Sets the message recipient: user ID of the recipient for one-to-one chat, group ID for group chat, or chat room ID for a chat room.
      //  cmdMsg.to = groupId
       // cmdMsg.addBody(cmdBody)
        ChatClient.getInstance().chatManager().sendMessage(cmdMsg)

    }


    var membersList = arrayListOf<String>()

    private fun fetchGroupMembers(){

        try {
            ChatClient.getInstance().groupManager().asyncFetchGroupMembers(
                groupId,
                "",
                100,
                object : ValueCallBack<CursorResult<String>> {
                    override fun onSuccess(value: CursorResult<String>?) {
                        try {
                            membersList = value?.data as ArrayList<String>
                            Log.d("fvlkvmf",membersList.size.toString())
                        }catch (e:Exception){
                            Log.d("vmvkfmvf",e.message.toString())
                        }
                    }

                    override fun onError(code: Int, error: String?) {
                        Log.d("fmkvmfv", error.toString() + "  error group Members")
                    }

                    override fun onProgress(progress: Int, status: String?) {
                    }
                })
        }catch (e:Exception){
            Log.d("Fvlvmfkmvf",e.message.toString() + "  exception")
        }
    }


}

object MConstants{

    //KEYS
    const val IS_INCOMING_CALL = "is_Incoming_Call"
    const val CHANNEL_NAME = "channel_Name"
    const val TARGET_USER_ID = "target_user_Id"
    const val NICKNAME = "nickname"
    const val CALL_TYPE = "call_Type"
    const val VOICE_OR_VIDEO = "voice_or_video"
    const val CALLER_ID = "caller_Id"
    const val CALL_OR_CHAT = "call_or_chat"
    const val ALERT = "alert"
    const val EM_PUSH_TITLE = "em_push_title"
    const val EM_PUSH_CONTENT = "em_push_content"
    const val CHAT_TYPE = "chatType"
    const val GROUP_MEMBERS_LIST = "membersList"
    const val GROUP_ID = "group_Id"
    const val GROUP_NAME = "group_Name"
    const val GROUP_DESCRIPTION = "group_Description"
    const val GROUP_OWNER = "group_Owner"

    //VALUES
    val SINGLE_CALL_TYPE_VALUE : String = "single_call_type"
    val GROUP_CALL_TYPE_VALUE : String = "group_call_type"
    val VOICE_CALL_VALUE : String = "voice_call"
    val VIDEO_CALL_VALUE : String = "video_call"
    val CHAT_VALUE : String = "chat"
    val CALL_VALUE : String = "call"

    const val ACCEPT_CALL_ACTION = "accept_call"
    const val REJECT_CALL_ACTION = "reject_call"

    const val ACCEPT_CALL_ACTION_VALUE = "accept"
    const val REJECT_CALL_ACTION_VALUE = "reject"

    const val CALL_ACTION = "call_action"

    const val CALL_NOTIFICATION_ID = 100




}
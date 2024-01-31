package com.example.chat__groupchatapp.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.chat__groupchatapp.AgoraTokenUtils.RtcTokenBuilder2
import com.example.chat__groupchatapp.AgoraUiKitUtils.PermissionsManager
import com.example.chat__groupchatapp.MyBroadCastReceiver
import com.example.chat__groupchatapp.R
import com.example.chat__groupchatapp.Utils.TokenBuilder
import com.example.chat__groupchatapp.data.remote.model.user.response.UserEntity
import com.example.chat__groupchatapp.databinding.ActivityAgoraChatUiactivityBinding
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
import io.agora.chat.PushConfigs
import io.agora.chat.TextMessageBody
import io.agora.chat.callkit.EaseCallKit
import io.agora.chat.callkit.bean.EaseCallUserInfo
import io.agora.chat.callkit.general.EaseCallEndReason
import io.agora.chat.callkit.general.EaseCallError
import io.agora.chat.callkit.general.EaseCallKitConfig
import io.agora.chat.callkit.general.EaseCallType
import io.agora.chat.callkit.listener.EaseCallGetUserAccountCallback
import io.agora.chat.callkit.listener.EaseCallKitListener
import io.agora.chat.callkit.listener.EaseCallKitTokenCallback
import io.agora.chat.uikit.EaseUIKit
import io.agora.chat.uikit.chat.EaseChatFragment
import io.agora.chat.uikit.chat.interfaces.OnMessageSendCallBack
import io.agora.chat.uikit.menu.EaseChatType
import io.agora.chat.uikit.utils.EaseFileUtils
import io.agora.push.PushConfig
import io.agora.push.PushHelper
import io.agora.push.PushListener
import io.agora.push.PushType
import org.json.JSONArray
import org.json.JSONObject


class AgoraChatUIActivity : AppCompatActivity() {

    private var group_Name: String? = null
    private var groupId: String?= null
    private var group_description: String?= null
    private var group_owner: String?= null
    private var userEntity: UserEntity?= null
    private var chat_type: Conversation.ConversationType? = null
    lateinit var binding : ActivityAgoraChatUiactivityBinding

    lateinit var  easeCallKit : EaseCallKit
    lateinit var  easeUIKit : EaseUIKit

    var fcmSenderId  = "264683372480"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgoraChatUiactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("fvknfvkfv",ChatClient.getInstance().currentUser.toString())

        ChatClient.getInstance().pushManager().getPushTemplate(object : CallBack,
            ValueCallBack<String> {
            override fun onSuccess() {
                Log.d("fbkkbngbg","Success tempate")
            }

            override fun onSuccess(value: String?) {
                Log.d("fbkkbngbg","Success tempate")
                Log.d("fbkkbngbg","Success tempate" + value.toString() + "  fkmnmkf")
            }

            override fun onError(code: Int, error: String?) {
                Log.d("fbkkbngbg","onError tempate")
            }

            override fun onProgress(progress: Int, status: String?) {
                Log.d("fbkkbngbg","onProgress tempate")
            }
        })


        easeCallKit = EaseCallKit.getInstance()
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
        initAgoraCallKitSdk()

        initFcmwithChatClient()
       // Log.d("fvnbknkgjbgb",PushHelper.getInstance().pushToken.toString())
        PushHelper.getInstance().register()


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



        binding.callButton.setOnClickListener {
            val userInfoMap2: MutableMap<String, Any> = HashMap()
            userInfoMap2["name"] = userEntity?.username.toString()
            userInfoMap2["name1"] = userEntity?.username.toString()



            easeCallKit.startSingleCall(EaseCallType.SINGLE_VIDEO_CALL,userEntity?.username,null,CallSingleBaseActivity::class.java)

        }
        easeCallKit.setCallKitListener(easeCallKitListener)

        binding.push.setOnClickListener {
            sendChatMessage()
        }

        ChatClient.getInstance().chatManager().addMessageListener(object : MessageListener{
            override fun onMessageReceived(messages: MutableList<ChatMessage>?) {
               sendBroadcast(Intent(this@AgoraChatUIActivity,MyBroadCastReceiver::class.java))
            }
        })

    }



    fun initChatUISDK(){
        val  chatoptions = ChatOptions()
        chatoptions.appKey = getString(R.string.APP_KEY)
        chatoptions.requireDeliveryAck = true
        chatoptions.autoLogin = false

        easeUIKit.init(this,chatoptions)
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

    fun initAgoraCallKitSdk(){
        val easeCallKitConfig = EaseCallKitConfig()
            easeCallKitConfig.callTimeOut = 30*1000
        easeCallKitConfig.agoraAppId = getString(R.string.APP_ID)
        easeCallKitConfig.isEnableRTCToken = true



        val userInfoMap: MutableMap<String, EaseCallUserInfo> = HashMap()
        userInfoMap["***"] = EaseCallUserInfo("****", null)
        userInfoMap["***"] = EaseCallUserInfo("****", null)
        easeCallKitConfig.setUserInfoMap(userInfoMap)

       easeCallKit.init(applicationContext,easeCallKitConfig)
       easeCallKit.registerVideoCallClass(CallSingleBaseActivity::class.java)
       easeCallKit.registerMultipleVideoClass(CallMultipleBaseActivity::class.java)



    }

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
        }

        override fun onRemoteUserJoinChannel(
            channelName: String?,
            userName: String?,
            uid: Int,
            callback: EaseCallGetUserAccountCallback?,
        ) {
            Log.d("fvmkf3232mv","onRemoteUserJoinChannel")
        }

        override fun onUserInfoUpdate(userName: String?) {
            super.onUserInfoUpdate(userName)
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun initFcmwithChatClient(){

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

    fun sendChatMessage(){
        val chatMessage = ChatMessage.createSendMessage(ChatMessage.Type.TXT)
        val textMessage = TextMessageBody("message content")
        chatMessage.to = userEntity?.username

        val pushObject = JSONObject()
        val titleArgs = JSONArray()
        val contentArgs = JSONArray()

        try{
            pushObject.put("Template_Name","test2")
            titleArgs.put("value1")
            pushObject.put("Title", titleArgs)
            contentArgs.put("value1")
            pushObject.put("Content",contentArgs)
        }catch (e:Exception){
            Log.d("Fknbkjbg",e.message.toString())
        }

        chatMessage.setAttribute("em_push_template",pushObject)
        chatMessage.body = textMessage

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

}
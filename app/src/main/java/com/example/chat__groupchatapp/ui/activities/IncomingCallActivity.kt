//package com.example.chat__groupchatapp.ui.activities
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import com.example.chat__groupchatapp.AgoraTokenUtils.RtcTokenBuilder2
//import com.example.chat__groupchatapp.Utils.TokenBuilder
////import io.agora.chat.callkit.EaseCallKit
////import io.agora.chat.callkit.databinding.EaseCallActivityCommingCallBinding
////import io.agora.chat.callkit.general.EaseCallEndReason
////import io.agora.chat.callkit.general.EaseCallError
////import io.agora.chat.callkit.general.EaseCallType
////import io.agora.chat.callkit.listener.EaseCallGetUserAccountCallback
////import io.agora.chat.callkit.listener.EaseCallKitListener
////import io.agora.chat.callkit.listener.EaseCallKitTokenCallback
////import io.agora.chat.callkit.ui.EaseCallBaseActivity
////import io.agora.chat.callkit.utils.EaseCallKitUtils
//import org.json.JSONObject
//
//class IncomingCallActivity : EaseCallBaseActivity() {
//
//    lateinit var binding : EaseCallActivityCommingCallBinding
//
//    lateinit var  easeCallKit : EaseCallKit
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = EaseCallActivityCommingCallBinding.inflate(layoutInflater)
//        setContentView(binding.root)
///*
//
//        val t = intent.getStringExtra("alert")
//        Log.d("fbkmkbbnm",t.toString())
//
//        intent?.extras?.keySet()?.forEach {
//            Log.d("lbmkbmgb",it.toString() + " -->>   "+ intent.extras?.get(it))
//        }
//*/
//
//
///*
//
//        val easeCallKit = EaseCallKit.getInstance()
//
//        val fromUserId = intent.getStringExtra("f")
//
//        val easeCallKitConfig = EaseCallKitConfig()
//        easeCallKitConfig.callTimeOut = 30*1000
//        easeCallKitConfig.agoraAppId = getString(R.string.APP_ID)
//     //   easeCallKitConfig.isEnableRTCToken = true
//
//
//        easeCallKit.init(applicationContext,easeCallKitConfig)
//        easeCallKit.registerVideoCallClass(CallSingleBaseActivity::class.java)
//        easeCallKit.registerMultipleVideoClass(CallMultipleBaseActivity::class.java)
//        easeCallKit.setCallKitListener(easeCallKitListener)
//
//
//
//
//*/
//
///*
//
//        try {
//            // Start a 1V1 call
//            val intent: Intent = Intent(this, CallSingleBaseActivity::class.java
//            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            val bundle = Bundle()
//            bundle.putBoolean("isComingCall", true)
//            bundle.putString("username", "2")
//            var channelName = EaseCallKitUtils.getRandomString(10)
//            bundle.putString("channelName", channelName)
//            intent.putExtras(bundle)
//            startActivity(intent)
//            finish()
//        }catch (e:Exception){
//            Log.d("fbkmfmkbmfb",e.message.toString())
//        }
//*/
//
//
//     //   EaseCallKit.getInstance().setCallKitListener(easeCallKitListener)
//
//
//
//
//    }
//    private val easeCallKitListener = object : EaseCallKitListener {
//        override fun onInviteUsers(
//            callType: EaseCallType?,
//            existMembers: Array<out String>?,
//            ext: JSONObject?,
//        ) {
//            Log.d("fvmkf3232mv","onInviteIsers")
//        }
//
//        override fun onEndCallWithReason(
//            callType: EaseCallType?,
//            channelName: String?,
//            reason: EaseCallEndReason?,
//            callTime: Long,
//        ) {
//            Log.d("fvmkf3232mv","onEndCallWithReason")
//        }
//
//        override fun onReceivedCall(
//            callType: EaseCallType?,
//            fromUserId: String?,
//            ext: JSONObject?,
//        ) {
//            Log.d("fvmkf3232mv","onReceivedCall")
//        }
//
//        override fun onGenerateRTCToken(
//            userId: String?,
//            channelName: String?,
//            callback: EaseCallKitTokenCallback?,
//        ) {
//            super.onGenerateRTCToken(userId, channelName, callback)
//
//            val token = TokenBuilder.getRtcTokenOfUid(this@IncomingCallActivity,userId?.toInt() ?: 0,channelName.toString(),
//                RtcTokenBuilder2.Role.ROLE_PUBLISHER)
//
//            callback?.onSetToken(token, userId?.toInt()?: 0)
//        }
//
//        override fun onCallError(type: EaseCallError?, errorCode: Int, description: String?) {
//
//            Log.d("fbfmbkfbmfg",type?.name.toString())
//            Log.d("fbfmbkfbmfg",type.toString())
//            Log.d("fbfmbkfbmfg",description.toString())
//            Log.d("fbfmbkfbmfg",errorCode.toString())
//            Log.d("fvmkf3232mv","onCallError")
//        }
//
//        override fun onInViteCallMessageSent() {
//            Log.d("fvmkf3232mv","onInViteCallMessageSent")
//        }
//
//        override fun onRemoteUserJoinChannel(
//            channelName: String?,
//            userName: String?,
//            uid: Int,
//            callback: EaseCallGetUserAccountCallback?,
//        ) {
//            Log.d("fvmkf3232mv","onRemoteUserJoinChannel")
//        }
//
//        override fun onUserInfoUpdate(userName: String?) {
//            super.onUserInfoUpdate(userName)
//        }
//    }
//
//
//
//}
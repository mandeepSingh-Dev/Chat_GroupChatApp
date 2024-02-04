package com.example.chat__groupchatapp

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.chat__groupchatapp.data.remote.model.AgoraNotificationItem
import com.example.chat__groupchatapp.ui.activities.*
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import io.agora.chat.ChatClient
import io.agora.chat.ChatMessage.ChatType
/*import io.agora.chat.callkit.EaseCallKit
import io.agora.chat.callkit.bean.EaseCallUserInfo
import io.agora.chat.callkit.general.EaseCallEndReason
import io.agora.chat.callkit.general.EaseCallError
import io.agora.chat.callkit.general.EaseCallKitConfig
import io.agora.chat.callkit.general.EaseCallType
import io.agora.chat.callkit.listener.EaseCallGetUserAccountCallback
import io.agora.chat.callkit.listener.EaseCallKitListener
import io.agora.chat.callkit.listener.EaseCallKitTokenCallback*/


class MyFirebaseMessagingService : FirebaseMessagingService() {


    override fun onMessageReceived(message: RemoteMessage) {

        Log.d("fmfkvnfmv","inMess")

//        initAgoraCallKitSdk()
//
//        val activityIntent = Intent(this,MyBroadCastReceiver::class.java)
//        message.data .forEach {
//            activityIntent.putExtra(it.key,it.value)
//        }
//
//
//
//        Log.d("fkvnjfnvf",message.data.get("f").toString())
//        CoroutineScope(Dispatchers.Main).launch {
//            EaseCallKit.getInstance().startSingleCall(
//                EaseCallType.SINGLE_VOICE_CALL,
//                message.data.get("2"),
//                null,
//                CallSingleBaseActivity::class.java
//            )
//        }
        val activityIntent = Intent(this,MyBroadCastReceiver::class.java)
        message.data.forEach {
            activityIntent.putExtra(it.key,it.value)
        }
        sendBroadcast(activityIntent)


    }


    fun createNotification(){
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(getString(R.string.default_notification_channel_id),getString(R.string.default_notification_channel_id),NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }


        val fullScreenIntent: Intent = Intent(this, LoginActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)

        val flags = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        val fullScreenPendingIntent = PendingIntent.getActivity(this, 3054, fullScreenIntent, flags)

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
            .setContentTitle(getString(R.string.app_name))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(fullScreenPendingIntent)
            .setOnlyAlertOnce(true)
            .setFullScreenIntent(fullScreenPendingIntent, true)

        notificationManager.notify(1,builder.build())

    }


    override fun onNewToken(token: String) {
        super.onNewToken(token)
       if(ChatClient.getInstance().isSdkInited) {
           ChatClient.getInstance().sendFCMTokenToServer(token)
       }
    }
/*
    val easeCallKitListener = object : EaseCallKitListener {
        override fun onInviteUsers(
            callType: EaseCallType?,
            existMembers: Array<out String>?,
            ext: JSONObject?,
        ) {
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


            val token = TokenBuilder.getRtcTokenOfUid(this@MyFirebaseMessagingService,userId?.toInt() ?: 0,channelName.toString(),
                RtcTokenBuilder2.Role.ROLE_PUBLISHER)

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
            // sendChatMessage()
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
*/

    // start notification id

    override fun handleIntent(intent: Intent?) {

        val activityIntent = Intent(this,MyBroadCastReceiver::class.java)
        intent?.extras?.keySet()?.forEach {
            Log.d("Fvkfnvbjkfnvf",it + "    " + intent?.extras?.get(it).toString())
            activityIntent.putExtra(it,intent.extras?.get(it).toString())
        }
        sendBroadcast(activityIntent)
    }


/*
    override fun handleIntent(intent: Intent?) {

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val intent1 = Intent(this,IncomingCallActivity::class.java)
        intent1.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val builder: NotificationCompat.Builder = GenerateNotificationBuilder.generateNotificatiobnBuilder(this,intent?.extras?.get("alert").toString())
        val notification = builder.build()

        notificationManager.notify(GenerateNotificationBuilder.NOTIFY_ID, notification)

*/
/*
     //   EaseCallKit.getInstance().setCallKitListener(easeCallKitListener)
     //   EaseCallKit.getInstance().notifier.notify(intent,"Hello","")
        try {

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(this,IncomingCallActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

       // if (!EasyUtils.isAppRunningForeground(this.applicationContext)) {
                val builder: NotificationCompat.Builder = GenerateNotificationBuilder.generateBaseFullIntentBuilder(intent,this,"Hello")
                val notification = builder.build()

                notificationManager.notify(GenerateNotificationBuilder.NOTIFY_ID, notification)
                //                if (Build.VERSION.SDK_INT < 26) {
//                    vibrateAndPlayTone(null);
//                }

            } catch (e: Exception) {
                Log.d("fkbnkfkbnf",e.message.toString())
                e.printStackTrace()
            }
     //   }
*//*


        val intent = Intent(this, IncomingCallActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
      //  startActivity(intent)


        */
/*
                intent?.extras?.keySet()?.forEach {
                    Log.d("fvlvkfvf",it.toString() + " -> " +intent?.extras?.get(it).toString())
                }

                val activityIntent1 = Intent(this,MyBroadCastReceiver::class.java)

                intent?.extras?.keySet()?.forEach {
                    activityIntent1.putExtra(it,intent.extras?.get(it).toString())
                }
                sendBroadcast(activityIntent1) *//*


    }
*/

/*
    override fun handleIntent(intent: Intent?) {
     //  super.handleIntent(intent)
*/
/*
        createNotification()
         *//*

        val alert = intent?.getStringExtra("alert")
        if(alert?.contains("video") == true){
            val intent1 = Intent(this, IncomingCallActivity::class.java)
            intent1.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent1.putExtra("f",intent.getStringExtra("f"))
            startActivity(intent1)
        }else{
            createNotification()
        }

        intent?.extras?.keySet()?.forEach {
            Log.d("Ffkbnfkbnf",it + " -> "+  intent.extras?.get(it).toString() + " handleIntent")
        }
    }
*/

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

        EaseCallKit.getInstance().init(applicationContext,easeCallKitConfig)

        EaseCallKit.getInstance().registerVideoCallClass(CallSingleBaseActivity::class.java)
        EaseCallKit.getInstance().registerMultipleVideoClass(CallMultipleBaseActivity::class.java)
    }
*/

/*
    fun initiateCallService(){
        try {
            val callHandler = CallHandler(applicationContext)
            callHandler.init()
            callHandler.startIncomingCall("sessionId", "Video")
        }catch (e:Exception){
            Log.d("initiateCallErRor",e.message + " Error")
            Toast.makeText(applicationContext,"unable to receive call due to ${e.message}",Toast.LENGTH_SHORT).show()
        }
    }
*/
}

class MyBroadCastReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {

        val jsonBody = intent?.extras?.getString("e")
        val item = Gson().fromJson<AgoraNotificationItem>(jsonBody,AgoraNotificationItem::class.java)

        if(item.call_or_chat == MConstants.CHAT_VALUE){
            createNotification(context, intent, isChat = true)
        }else if(item.reject_call != null){
            stopCallingScreenActivity(context,item)
        }else{
          //  if(isForeground(context,context?.packageName.toString())){
            //    createNotification(context,intent ,isChat = false)
           // }else{
                startCallingScreenActivity(context,item)
           // }
        }


     /*    val intent1 = Intent(context,IncomingCallActivity::class.java)
        intent1.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP

        intent1.setAction("android.intent.action.MAIN");
        intent1.addCategory("android.intent.category.LAUNCHER");

        intent?.extras?.let { intent1.putExtras(it) };
        context?.startActivity(intent1) */
    }



    @SuppressLint("SuspiciousIndentation")
    fun createNotification(context: Context?, intent: Intent?, isChat : Boolean = true ){
        val notificationManager = context?.getSystemService(FirebaseMessagingService.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(context?.getString(R.string.default_notification_channel_id),context?.getString(R.string.default_notification_channel_id),NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val fullScreenIntent: Intent = Intent(context, UsersGroupActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT else PendingIntent.FLAG_UPDATE_CURRENT
        val fullScreenPendingIntent = PendingIntent.getActivity(context, 1, fullScreenIntent, flags)

        val jsonBody = intent?.extras?.getString("e")
        val item = Gson().fromJson<AgoraNotificationItem>(jsonBody,AgoraNotificationItem::class.java)

        var isAutocancel = false
            try {
              isAutocancel  =   !(item.chatType == ChatType.GroupChat.toString() || item.chatType == ChatType.Chat.toString())
        }catch (e:Exception){
            isAutocancel = true
        }

        var title = "Demo"
        kotlin.runCatching {
            title = item.chatType.toString()
        }
        var contentText = "New Message"
        kotlin.runCatching {
            contentText = item.alert.toString()
        }.onFailure {
            contentText = intent?.extras?.getString("alert").toString()
        }

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, context.getString(R.string.default_notification_channel_id))
            .setContentTitle(title)
            .setContentText(contentText)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setSmallIcon(R.drawable.icons8_remind_app).setContentIntent(fullScreenPendingIntent)
            .setFullScreenIntent(fullScreenPendingIntent, true)

        if(isChat){
            builder.setCategory(NotificationCompat.CATEGORY_MESSAGE)
            builder.setAutoCancel(true)
        }else{
            builder.setCategory(NotificationCompat.CATEGORY_CALL)
            builder.setAutoCancel(false)
          //  builder.addAction(NotificationCompat.Action.Builder(R.drawable.baseline_call_end_24,"Reject",))
        }


        notificationManager.notify(GenerateNotificationBuilder.NOTIFY_ID,builder.build())
    }

    fun isForeground(context: Context?,myPackage: String): Boolean {
        val manager =context?.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val runningTaskInfo = manager.getRunningTasks(1)
        val isForeground = if(runningTaskInfo.isNotEmpty()){
            val componentInfo = runningTaskInfo[0].topActivity
            componentInfo?.packageName == myPackage
        }else{
            false
        }
        return isForeground
    }

    private fun startCallingScreenActivity(context: Context?, item : AgoraNotificationItem){

        var callingScreenActivity : Class<out AppCompatActivity>? = null

        if(item.voice_or_video != null){
             callingScreenActivity = if(item.voice_or_video == MConstants.VOICE_CALL_VALUE){
                // if(item.call_Type == MConstants.SINGLE_CALL_TYPE_VALUE){}else{}
                VoiceCallActivity::class.java
            }else{
                 if(item.call_Type == MConstants.SINGLE_CALL_TYPE_VALUE){
                     VideoCallActivity::class.java
                 }else{
                     GroupVideoCallActivity::class.java
                 }

            }
        }


        val intent = Intent(context,callingScreenActivity)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP

        if(item.is_Incoming_Call != null){
           intent.putExtra(MConstants.IS_INCOMING_CALL,item.is_Incoming_Call.toString())
        }
        if(item.channel_Name != null){
        intent.putExtra(MConstants.CHANNEL_NAME,item.channel_Name.toString())
            }
        if(item.user_Id != null){
           intent.putExtra(MConstants.TARGET_USER_ID,item.user_Id.toString())
        }
        if(item.call_Type != null){
            intent.putExtra(MConstants.CALL_TYPE,item.call_Type.toString())
            }
        if(item.voice_or_video != null){
           intent.putExtra(MConstants.VOICE_OR_VIDEO,item.voice_or_video.toString())
                }
        if(item.caller_Id != null){
            intent.putExtra(MConstants.CALLER_ID,item.caller_Id.toString())
        }
        if(item.call_or_chat != null){
           intent.putExtra(MConstants.CALL_OR_CHAT,item.call_or_chat.toString())
        }
        if(item.alert != null){
            intent.putExtra(MConstants.ALERT,item.alert.toString())
        }
        if(item.chatType != null){
            intent.putExtra(MConstants.CHAT_TYPE,item.chatType.toString())
        }
        context?.startActivity(intent)

    }

    private fun stopCallingScreenActivity(context: Context?, item: AgoraNotificationItem?) {



        val voiceCallActivity = "com.example.chat__groupchatapp.ui.activities.VoiceCallActivity"
        val videoCallActivity = "com.example.chat__groupchatapp.ui.activities.VideoCallActivity"
        val groupVideoCallActivity = "com.example.chat__groupchatapp.ui.activities.GroupVideoCallActivity"

        val activityManager = context?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningActivities = activityManager.getRunningTasks(1)
        if(runningActivities.isNotEmpty()){

            runningActivities.forEach {
                val topActivityClassName = it.topActivity?.className
                if(topActivityClassName == voiceCallActivity || topActivityClassName == videoCallActivity || topActivityClassName == groupVideoCallActivity)
                {
                    if(topActivityClassName == voiceCallActivity){
                       Log.d("Fvlfmvkf","voiceCallActivity")
                        val intent = Intent(context,VoiceCallActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        intent.putExtra(MConstants.REJECT_CALL_ACTION,MConstants.REJECT_CALL_ACTION_VALUE)
                        context?.startActivity(intent)
                    }else if(topActivityClassName == videoCallActivity){
                        Log.d("Fvlfmvkf","videoCallActivity")
                        val intent = Intent(context,VideoCallActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        intent.putExtra(MConstants.REJECT_CALL_ACTION,MConstants.REJECT_CALL_ACTION_VALUE)
                        context?.startActivity(intent)
                    }else if(topActivityClassName == groupVideoCallActivity){
                        Log.d("Fvlfmvkf","groupVideoCallActivity")
                        val intent = Intent(context,GroupVideoCallActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        intent.putExtra(MConstants.REJECT_CALL_ACTION,MConstants.REJECT_CALL_ACTION_VALUE)
                        context?.startActivity(intent)
                    }
                }
            }
        }
    }
}

class CallActionsReciever : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
         when(intent?.action){
             MConstants.ACCEPT_CALL_ACTION -> {

             }
             MConstants.REJECT_CALL_ACTION -> {

             }
         }
    }

}
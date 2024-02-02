package com.example.chat__groupchatapp

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
import androidx.core.app.NotificationCompat
import com.example.chat__groupchatapp.AgoraTokenUtils.RtcTokenBuilder2
import com.example.chat__groupchatapp.Utils.TokenBuilder
import com.example.chat__groupchatapp.data.remote.model.AgoraNotificationItem
import com.example.chat__groupchatapp.ui.activities.CallMultipleBaseActivity
import com.example.chat__groupchatapp.ui.activities.CallSingleBaseActivity
import com.example.chat__groupchatapp.ui.activities.GenerateNotificationBuilder
import com.example.chat__groupchatapp.ui.activities.LoginActivity
import com.example.chat__groupchatapp.ui.activities.UsersGroupActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import io.agora.chat.ChatClient
import io.agora.chat.ChatMessage.ChatType
import io.agora.chat.callkit.EaseCallKit
import io.agora.chat.callkit.bean.EaseCallUserInfo
import io.agora.chat.callkit.general.EaseCallEndReason
import io.agora.chat.callkit.general.EaseCallError
import io.agora.chat.callkit.general.EaseCallKitConfig
import io.agora.chat.callkit.general.EaseCallType
import io.agora.chat.callkit.listener.EaseCallGetUserAccountCallback
import io.agora.chat.callkit.listener.EaseCallKitListener
import io.agora.chat.callkit.listener.EaseCallKitTokenCallback
import org.json.JSONObject


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
}

class MyBroadCastReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context,"Received broadcast reciever",Toast.LENGTH_SHORT).show()

        createNotification(context,intent)



     /*    val intent1 = Intent(context,IncomingCallActivity::class.java)
        intent1.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP

        intent1.setAction("android.intent.action.MAIN");
        intent1.addCategory("android.intent.category.LAUNCHER");

        intent?.extras?.let { intent1.putExtras(it) };
        context?.startActivity(intent1) */
    }

    fun createNotification(context: Context?, intent: Intent?){
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
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setSmallIcon(R.drawable.icons8_remind_app).setContentIntent(fullScreenPendingIntent)
            .setAutoCancel(isAutocancel)
            .setFullScreenIntent(fullScreenPendingIntent, true)

        if(isAutocancel){
            builder.addAction(NotificationCompat.Action.Builder(io.agora.chat.callkit.R.drawable.call_answer,
               "Accept",fullScreenPendingIntent).build())
            builder.addAction(NotificationCompat.Action.Builder( io.agora.chat.callkit.R.drawable.call_end,"Reject",fullScreenPendingIntent).build())
        }

        notificationManager.notify(GenerateNotificationBuilder.NOTIFY_ID,builder.build())

   /*      if(!isForeground(context,context.packageName)) {
            val intent = Intent(context, IncomingCallActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
          //  context.startActivity(intent)
        //    notificationManager.notify(1,builder.build())

         //   Toast.makeText(context,"isForeground NOt",Toast.LENGTH_SHORT).show()
        }else{
       //     Toast.makeText(context,"isForeground",Toast.LENGTH_SHORT).show()
          //  notificationManager.notify(1,builder.build())
        }
 */
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



}
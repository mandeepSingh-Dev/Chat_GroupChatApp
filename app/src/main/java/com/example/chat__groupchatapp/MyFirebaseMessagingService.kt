package com.example.chat__groupchatapp

import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.RingtoneManager
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import com.example.chat__groupchatapp.data.remote.model.AgoraNotificationItem
import com.example.chat__groupchatapp.ui.activities.*
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import io.agora.chat.ChatClient
import io.agora.chat.ChatMessage.ChatType
import io.agora.chat.ChatOptions
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig

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

  /*       val activityIntent = Intent(this,MyBroadCastReceiver::class.java)
        message.data.forEach {
            activityIntent.putExtra(it.key,it.value)
        }
        sendBroadcast(activityIntent)
 */

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
       if(ChatClient.getInstance().isSdkInited) {
           ChatClient.getInstance().sendFCMTokenToServer(token)
       }
    }

    override fun handleIntent(intent: Intent?) {

        val activityIntent = Intent(this,MyBroadCastReceiver::class.java)
        intent?.extras?.keySet()?.forEach {
            Log.d("Fvkfnvbjkfnvf",it + "    " + intent.extras?.get(it).toString())
            activityIntent.putExtra(it,intent.extras?.get(it).toString())
        }
        sendBroadcast(activityIntent)


    }
}

class MyBroadCastReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {

        //"e" contains jsonBody of push notification.
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
            createCallFullScreenNotification(context,intent)
               // startCallingScreenActivity(context,item)
           // }
        }


     /*    val intent1 = Intent(context,IncomingCallActivity::class.java)
        intent1.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP

        intent1.setAction("android.intent.action.MAIN");
        intent1.addCategory("android.intent.category.LAUNCHER");

        intent?.extras?.let { intent1.putExtras(it) };
        context?.startActivity(intent1) */
    }


    fun createCallFullScreenNotification(context: Context?, intent : Intent?){
        val notificationManager = context?.getSystemService(FirebaseMessagingService.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(context?.getString(R.string.default_notification_channel_id),context?.getString(R.string.default_notification_channel_id),NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)

            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setLegacyStreamType(AudioManager.STREAM_RING)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE).build()

            notificationChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE),audioAttributes)
        }
        val jsonBody = intent?.extras?.getString("e")
        val item = Gson().fromJson<AgoraNotificationItem>(jsonBody,AgoraNotificationItem::class.java)

        var callingScreenActivity : Class<out AppCompatActivity>? = null

        var title : String = ""
        var contentText : String = ""
        var caller : String = ""

        if(item.voice_or_video != null){
            callingScreenActivity = if(item.voice_or_video == MConstants.VOICE_CALL_VALUE){
                if(item.call_Type == MConstants.SINGLE_CALL_TYPE_VALUE){
                    caller = item.caller_Id.toString()
                    title = "Agora voice call"
                    contentText = "Agora Voice call from ${item.caller_Id}"
                }else{
                    caller = item.group_Name.toString()
                    title = "Agora voice group call"
                    contentText = "Agora voice group call from ${item.group_Id ?: item.group_Name}"
                }
                VoiceCallActivity::class.java
            }else{
                if(item.call_Type == MConstants.SINGLE_CALL_TYPE_VALUE){
                    caller = item.caller_Id.toString()
                    title = "Agora video call"
                    contentText = "Agora Video call from ${item.caller_Id}"

                    VideoCallActivity::class.java
                }else{
                    caller = item.group_Name.toString()
                    title = "Agora video call"
                    contentText = "Agora video group call from ${item.group_Id ?: item.group_Name}"
                    GroupVideoCallActivity::class.java
                }

            }
        }

        val acceptFullScreenIntent = callingScreenActivity?.let { getCallingScreenIntent(context,item, it,MConstants.ACCEPT_CALL_ACTION_VALUE) }
        val rejectFullScreenIntent = callingScreenActivity?.let { getCallingScreenIntent(context,item, it,MConstants.REJECT_CALL_ACTION_VALUE) }
        val fullScreenIntent = callingScreenActivity?.let { getCallingScreenIntent(context,item, it) }



        val flags = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        val acceptCallingScreenPendingIntent = PendingIntent.getActivity(context, 1, acceptFullScreenIntent, flags)
        val rejectCallingScreenPendingIntent = PendingIntent.getActivity(context, 2, rejectFullScreenIntent, flags)
        val fullScreenPendingIntent = PendingIntent.getActivity(context, 3, fullScreenIntent, flags)


        val incomingCaller = Person.Builder()
            .setName(caller)
            .setImportant(true)
            .build()


        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, context.getString(R.string.default_notification_channel_id))
            .setContentTitle(title)
            .setContentText(contentText)
            .setSmallIcon(R.drawable.icons8_remind_app)
            .setContentIntent(fullScreenPendingIntent)
          //  .addAction(NotificationCompat.Action.Builder(R.drawable.baseline_call_end_24,"Reject",rejectCallingScreenPendingIntent).build())
          //  .addAction(NotificationCompat.Action.Builder(androidx.core.R.drawable.ic_call_answer,"Accept",acceptCallingScreenPendingIntent).build())
            .setFullScreenIntent(fullScreenPendingIntent, true)
           // .setCategory(NotificationCompat.CATEGORY_CALL)
            //.setVibrate(longArrayOf(1000,1000,1000,1000,1000))
            .setStyle(NotificationCompat.CallStyle.forIncomingCall(incomingCaller, rejectCallingScreenPendingIntent, acceptCallingScreenPendingIntent))
            .addPerson(incomingCaller)
            //  builder.addAction(NotificationCompat.Action.Builder(R.drawable.baseline_call_end_24,"Reject",))

        notificationManager.notify(MConstants.CALL_NOTIFICATION_ID,builder.build())

    }

    fun getCallingScreenIntent(context: Context?,item : AgoraNotificationItem, callingScreenActivity : Class<out AppCompatActivity>, accept_reject : String? = null): Intent {
        val fullScreenIntent = Intent(context, callingScreenActivity)
        fullScreenIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP

        if(item.is_Incoming_Call != null){
            fullScreenIntent.putExtra(MConstants.IS_INCOMING_CALL,item.is_Incoming_Call.toString())
        }
        if(item.channel_Name != null){
            fullScreenIntent.putExtra(MConstants.CHANNEL_NAME,item.channel_Name.toString())
        }
        if(item.user_Id != null){
            fullScreenIntent.putExtra(MConstants.TARGET_USER_ID,item.user_Id.toString())
        }
        if(item.call_Type != null){
            fullScreenIntent.putExtra(MConstants.CALL_TYPE,item.call_Type.toString())
        }
        if(item.voice_or_video != null){
            fullScreenIntent.putExtra(MConstants.VOICE_OR_VIDEO,item.voice_or_video.toString())
        }
        if(item.caller_Id != null){
            fullScreenIntent.putExtra(MConstants.CALLER_ID,item.caller_Id.toString())
        }
        if(item.call_or_chat != null){
            fullScreenIntent.putExtra(MConstants.CALL_OR_CHAT,item.call_or_chat.toString())
        }
        if(item.alert != null){
            fullScreenIntent.putExtra(MConstants.ALERT,item.alert.toString())
        }
        if(item.chatType != null){
            fullScreenIntent.putExtra(MConstants.CHAT_TYPE,item.chatType.toString())
        }
        if(item.group_Id != null){
            fullScreenIntent.putExtra(MConstants.GROUP_ID,item.group_Id)
        }
        if(item.group_Name != null){
            fullScreenIntent.putExtra(MConstants.GROUP_NAME,item.group_Name)
        }
        if(item.group_Description != null){
            fullScreenIntent.putExtra(MConstants.GROUP_DESCRIPTION,item.group_Description)
        }
        if(item.group_Owner != null){
            fullScreenIntent.putExtra(MConstants.GROUP_OWNER,item.group_Owner)
        }

        val call_action = if(accept_reject == MConstants.ACCEPT_CALL_ACTION_VALUE)
         MConstants.ACCEPT_CALL_ACTION_VALUE
        else if(accept_reject == MConstants.REJECT_CALL_ACTION_VALUE)
           MConstants.REJECT_CALL_ACTION_VALUE
        else
            null

        Log.d("fvfkvkfvf",call_action.toString())
        fullScreenIntent.putExtra(MConstants.CALL_ACTION,call_action)

        return fullScreenIntent

    }


    @SuppressLint("SuspiciousIndentation")
    fun createNotification(context: Context?, intent: Intent?, isChat : Boolean = true ) {
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
            .setSmallIcon(R.drawable.icons8_remind_app).setContentIntent(fullScreenPendingIntent)
            //   .setFullScreenIntent(fullScreenPendingIntent, true)

        if(isChat){
            builder.setAutoCancel(true)
        }else{
            builder.setCategory(NotificationCompat.CATEGORY_CALL)
            builder.setAutoCancel(false)
          //  builder.addAction(NotificationCompat.Action.Builder(R.drawable.baseline_call_end_24,"Reject",))
        }


        notificationManager.notify(101,builder.build())
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
       if(item.group_Id != null){
            intent.putExtra(MConstants.GROUP_ID,item.group_Id)
        }
       if(item.group_Name != null){
            intent.putExtra(MConstants.GROUP_NAME,item.group_Name)
        }
       if(item.group_Description != null){
            intent.putExtra(MConstants.GROUP_DESCRIPTION,item.group_Description)
        }
       if(item.group_Owner != null){
            intent.putExtra(MConstants.GROUP_OWNER,item.group_Owner)
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
                        context.startActivity(intent)





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
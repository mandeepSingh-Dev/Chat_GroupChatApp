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
import com.example.chat__groupchatapp.ui.activities.IncomingCallActivity
import com.example.chat__groupchatapp.ui.activities.LoginActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.agora.chat.ChatClient
import io.agora.chat.callkit.EaseCallKit


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {


        val activityIntent = Intent(this,MyBroadCastReceiver::class.java)
        message.data .forEach {
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

    override fun handleIntent(intent: Intent?) {

        intent?.extras?.keySet()?.forEach {
            Log.d("fvlvkfvf",it.toString() + " -> " +intent?.extras?.get(it).toString())
        }

        val activityIntent1 = Intent(this,MyBroadCastReceiver::class.java)

        intent?.extras?.keySet()?.forEach {
            activityIntent1.putExtra(it,intent.extras?.get(it).toString())
        }

        sendBroadcast(activityIntent1)

    }

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

}

class MyBroadCastReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context,"Received broadcast reciever",Toast.LENGTH_SHORT).show()
        createNotification(context)



     /*    val intent1 = Intent(context,IncomingCallActivity::class.java)
        intent1.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP

        intent1.setAction("android.intent.action.MAIN");
        intent1.addCategory("android.intent.category.LAUNCHER");

        intent?.extras?.let { intent1.putExtras(it) };
        context?.startActivity(intent1) */
    }

    fun createNotification(context: Context?){
        val notificationManager = context?.getSystemService(FirebaseMessagingService.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(context?.getString(R.string.default_notification_channel_id),context?.getString(R.string.default_notification_channel_id),NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val fullScreenIntent: Intent = Intent(context, IncomingCallActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)

        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT else PendingIntent.FLAG_UPDATE_CURRENT
        val fullScreenPendingIntent = PendingIntent.getActivity(context, 1, fullScreenIntent, flags)

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, context.getString(R.string.default_notification_channel_id))
            .setContentTitle(context.getString(R.string.app_name))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(fullScreenPendingIntent)
            .setOnlyAlertOnce(true)
            .setFullScreenIntent(fullScreenPendingIntent, true)

        notificationManager.notify(1,builder.build())

        val intent = Intent(context,IncomingCallActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)

    }

    fun isForeground(context: Context?,myPackage: String): Boolean {
        val manager =context?.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val runningTaskInfo = manager.getRunningTasks(1)
        val componentInfo = runningTaskInfo[0].topActivity
        return componentInfo?.packageName == myPackage
    }
}
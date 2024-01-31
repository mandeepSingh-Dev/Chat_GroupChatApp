package com.example.chat__groupchatapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.tooling.data.ContextCache
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import com.example.chat__groupchatapp.ui.activities.SplashScreen
import com.example.chat__groupchatapp.ui.activities.UsersGroupActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.agora.chat.ChatClient

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.d("fvkjfjvnfj",message.notification?.title.toString() + " rfrrfrr")
        createNotification()

        val intent = Intent(this,MyBroadCastReceiver::class.java)
        sendBroadcast(intent)


      //  Toast.makeText(this,"Received",Toast.LENGTH_SHORT).show()
    }

    fun createNotification(){
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(getString(R.string.default_notification_channel_id),getString(R.string.default_notification_channel_id),NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }


        val fullScreenIntent = Intent(this,SplashScreen::class.java)
        fullScreenIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        val pendingIntent = PendingIntent.getActivity(this,101,fullScreenIntent,PendingIntent.FLAG_MUTABLE)

        val notification = NotificationCompat.Builder(this,getString(R.string.default_notification_channel_id))
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Video Call")
            .setContentTitle("Video Call initiated")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setContentIntent(pendingIntent)
            .setFullScreenIntent(pendingIntent,true).build()

         notificationManager.notify(10,notification)
    }


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("vvkmfkvmf",token.toString())
       if(ChatClient.getInstance().isSdkInited) {
           ChatClient.getInstance().sendFCMTokenToServer(token)
       }
    }

    override fun handleIntent(intent: Intent?) {
     //   super.handleIntent(intent)

        createNotification()

        val intent = Intent(this,MyBroadCastReceiver::class.java)
        sendBroadcast(intent)
    }

    override fun handleIntentOnMainThread(intent: Intent?): Boolean {
        val intent = Intent(this,MyBroadCastReceiver::class.java)
        createNotification()
        sendBroadcast(intent)
        return super.handleIntentOnMainThread(intent)
    }
}

class MyBroadCastReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context,"Received",Toast.LENGTH_SHORT).show()
        createNotification(context)
    }

    fun createNotification(context: Context?){
        val notificationManager = context?.getSystemService(FirebaseMessagingService.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(context?.getString(R.string.default_notification_channel_id),context?.getString(R.string.default_notification_channel_id),NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }


        val fullScreenIntent = Intent(context,SplashScreen::class.java)
        fullScreenIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        val pendingIntent = PendingIntent.getActivity(context,101,fullScreenIntent,PendingIntent.FLAG_MUTABLE)

        val notification = NotificationCompat.Builder(context,context?.getString(R.string.default_notification_channel_id).toString())
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Video Call")
            .setContentTitle("Video Call initiated")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setContentIntent(pendingIntent)
            .setFullScreenIntent(pendingIntent,true).build()

        notificationManager.notify(10,notification)
    }
}
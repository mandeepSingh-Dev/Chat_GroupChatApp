package com.example.chat__groupchatapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.agora.chat.ChatClient

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("fvkjfjvnfj",message.notification?.title.toString() + " rfrrfrr")
       val intent = Intent(this,MyBroadCastReceiver::class.java)
        sendBroadcast(intent)
      //  Toast.makeText(this,"Received",Toast.LENGTH_SHORT).show()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("vvkmfkvmf",token.toString())
       if(ChatClient.getInstance().isSdkInited) {
           ChatClient.getInstance().sendFCMTokenToServer(token)
       }
    }

    override fun handleIntent(intent: Intent?) {
        super.handleIntent(intent)
        val intent = Intent(this,MyBroadCastReceiver::class.java)
        sendBroadcast(intent)
    }

    override fun handleIntentOnMainThread(intent: Intent?): Boolean {
        val intent = Intent(this,MyBroadCastReceiver::class.java)
        sendBroadcast(intent)
        return super.handleIntentOnMainThread(intent)
    }
}

class MyBroadCastReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context,"Received",Toast.LENGTH_SHORT).show()
    }
}
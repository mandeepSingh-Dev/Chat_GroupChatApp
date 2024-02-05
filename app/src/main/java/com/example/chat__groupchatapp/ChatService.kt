package com.example.chat__groupchatapp

import android.app.IntentService
import android.app.Service
import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import io.agora.MessageListener
import io.agora.chat.ChatClient
import io.agora.chat.ChatMessage

class ChatService : JobService() {

/*

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val currentUser = ChatClient.getInstance().currentUser.toString()

        Log.d("Fvlfmvkfmv",currentUser.toString())

        ChatClient.getInstance().chatManager()?.addMessageListener(object : MessageListener{
            override fun onMessageReceived(messages: MutableList<ChatMessage>?) {

              //  Toast.makeText(applicationContext,"recieved",Toast.LENGTH_SHORT).show()
                Log.d("gbkjbngkbjg","Message Received")
            }

            override fun onCmdMessageReceived(messages: MutableList<ChatMessage>?) {
                super.onCmdMessageReceived(messages)
               // Toast.makeText(applicationContext,"recieved",Toast.LENGTH_SHORT).show()
                Log.d("gbkjbngkbjg","CMD Message Received")
            }
        })

        return START_STICKY
    }
*/

  /*   override fun onBind(intent: Intent?): IBinder? {
        return null
    } */

    override fun onStartJob(params: JobParameters?): Boolean {
        Toast.makeText(this,"onStartJob",Toast.LENGTH_SHORT).show()

        val currentUser = ChatClient.getInstance().currentUser.toString()

        Log.d("Fvlfmvkfmv",currentUser.toString())

        ChatClient.getInstance().chatManager()?.addMessageListener(object : MessageListener {
            override fun onMessageReceived(messages: MutableList<ChatMessage>?) {

                val intent = Intent(this@ChatService,JobBroadcastReceiver::class.java)
                sendBroadcast(intent)
                  Toast.makeText(this@ChatService,"recieved",Toast.LENGTH_SHORT).show()
                Log.d("gbkjbngkbjg","Message Received")
            }

            override fun onCmdMessageReceived(messages: MutableList<ChatMessage>?) {
                super.onCmdMessageReceived(messages)
                // Toast.makeText(applicationContext,"recieved",Toast.LENGTH_SHORT).show()
                Log.d("gbkjbngkbjg","CMD Message Received")
            }
        })

        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
      return true
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)

    }


    override fun onDestroy() {

        Log.d("Fvlvlfmvf",ChatClient.getInstance().currentUser.toString() + " onDestroy")
        super.onDestroy()

        val chatServiceComponentName = ComponentName(this,ChatService::class.java)
        val jobInfo = JobInfo.Builder(101, chatServiceComponentName)

        val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(jobInfo.build())
    }
}

class JobBroadcastReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context,"receive",Toast.LENGTH_SHORT).show()
    }
}
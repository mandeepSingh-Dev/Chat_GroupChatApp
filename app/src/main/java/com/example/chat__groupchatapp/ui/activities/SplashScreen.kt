package com.example.chat__groupchatapp.ui.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.ACTION_MANAGE_APP_USE_FULL_SCREEN_INTENT
import androidx.core.app.NotificationCompat
import com.example.chat__groupchatapp.AgoraChatHelper
import com.example.chat__groupchatapp.ChatService
import com.example.chat__groupchatapp.R
import com.example.chat__groupchatapp.Utils.getExpiryInSeconds
import com.example.chat__groupchatapp.databinding.ActivitySplashScreenBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {

    var agoraChatHelper : AgoraChatHelper? = null

    lateinit var binding : ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()
        agoraChatHelper = AgoraChatHelper()
        agoraChatHelper?.setUpChatClient(this)


    /*     val chatServiceComponentName = ComponentName(this,ChatService::class.java)
        val jobInfo = JobInfo.Builder(101, chatServiceComponentName)
            .setPeriodic(10)

        val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(jobInfo.build())
 */

        CoroutineScope(Dispatchers.Main).launch {
        delay(2000)
            if(agoraChatHelper?.isUserLoggedIn() == true){
                startActivity(Intent(this@SplashScreen,UsersGroupActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this@SplashScreen,LoginActivity::class.java))
                finish()
            }
        }


        getExpiryInSeconds(5)
    }
    fun createNotificationChannel(){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(getString(R.string.default_notification_channel_id),getString(R.string.default_notification_channel_id),
                NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }


}
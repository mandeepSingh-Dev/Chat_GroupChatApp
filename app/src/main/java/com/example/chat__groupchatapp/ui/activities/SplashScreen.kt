package com.example.chat__groupchatapp.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chat__groupchatapp.AgoraChatHelper
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

        agoraChatHelper = AgoraChatHelper()
        agoraChatHelper?.setUpChatClient(this)

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
}
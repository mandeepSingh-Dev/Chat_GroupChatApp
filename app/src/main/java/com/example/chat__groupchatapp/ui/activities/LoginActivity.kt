package com.example.chat__groupchatapp.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.example.chat__groupchatapp.R
import com.example.chat__groupchatapp.Utils.Constants
import com.example.chat__groupchatapp.Utils.Widgets.BounceButton
import com.example.chat__groupchatapp.Utils.showToast
import com.example.chat__groupchatapp.databinding.ActivityMainBinding
import io.agora.CallBack
import io.agora.ConnectionListener
import io.agora.chat.ChatClient
import io.agora.chat.ChatOptions

class LoginActivity : AppCompatActivity() {
    
    lateinit var binding : ActivityMainBinding

      var chatClient : ChatClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setComposableButton("Login")

        setUpChatClient()
     //   setListeners()

        binding.registerTextView.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }



    private fun setComposableButton(buttonText : String){

        binding.composeView.setContent {
            BounceButton(buttonText, onButtonClick = {
                loginToChat(binding.userNameEditText.text?.trim().toString(), binding.passwordEditText.text?.trim().toString())
            })
        }
    }


    private fun setUpChatClient(){
        try {
            val optins = ChatOptions()
            optins.appKey = getString(R.string.APP_KEY)

             chatClient = ChatClient.getInstance()
            chatClient?.init(this,optins)
            chatClient?.setDebugMode(true)

            setListeners()
        }catch (e:Exception){
            showToast(e.message)
        }
    }

    private fun setListeners(){
        chatClient?.addConnectionListener(object : ConnectionListener{
            override fun onConnected() {
                showToast("onConnected")
                showToast("onConnected")
            }

            override fun onDisconnected(errorCode: Int) {
                showToast("onDisconnected")
            }

            override fun onLogout(errorCode: Int, info: String?) {
                super.onLogout(errorCode, info)
                showToast("onLogout")
            }

            override fun onTokenExpired() {
                super.onTokenExpired()
                showToast("onTokenExpired")
            }

            override fun onTokenWillExpire() {
                super.onTokenWillExpire()
                showToast("onTokenWillExpire")
            }
        })
    }

    private fun loginToChat(user : String, pwd : String){
        binding.progressBar.visibility = View.VISIBLE
        chatClient?.login(user,pwd,object : CallBack{
            override fun onSuccess() {
                runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                }
                val intent = Intent(this@LoginActivity, UsersGroupActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)

                val sharedPreferences = getSharedPreferences(Constants.sharedPrefName, MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString(Constants.user,user)
                editor.commit()
                editor.apply()


                showToast(message = "Login onSuccess")
            }
            override fun onError(code: Int, error: String?) {
                runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                }
                showToast(message = "Login onError $error")
            }

            override fun onProgress(progress: Int, status: String?) {
                super.onProgress(progress, status)
            }
        })
    }

    fun logout(){
        chatClient?.logout(true,object : CallBack{
            override fun onSuccess() {
                showToast(message = "Logout onSuccess")
            }

            override fun onError(code: Int, error: String?) {
                showToast(message = "Logout onError $error")
            }

        })
    }

}









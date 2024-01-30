package com.example.chat__groupchatapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.chat__groupchatapp.Utils.Widgets.BounceButton
import com.example.chat__groupchatapp.Utils.showSnackbar
import com.example.chat__groupchatapp.Utils.showToast
import com.example.chat__groupchatapp.data.remote.RetrofitClient
import com.example.chat__groupchatapp.data.remote.model.user.request.register.RegisterUserRequestBody
import com.example.chat__groupchatapp.databinding.ActivityRegisterBinding
import io.agora.CallBack
import io.agora.ConnectionListener
import io.agora.chat.ChatClient
import io.agora.chat.ChatOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    lateinit var binding : ActivityRegisterBinding

    var chatClient : ChatClient? = null
    var appKey = "611085667#1266092"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpChatClient()


        binding.composeView.setContent {
            BounceButton("Register", onButtonClick = {
                val username = binding.userNameEditText.text?.toString()?.trim().toString()
                val pwd = binding.passwordEditText.text?.toString()?.trim().toString()
                val cnfPwd = binding.confirmpasswordEditText.text?.toString()?.trim().toString()
                val nickName = binding.nickNameEditText.text?.toString()

                if(pwd == cnfPwd){

                    CoroutineScope(Dispatchers.Main).launch {
                        if(!username.isNullOrEmpty() && !pwd.isNullOrEmpty() && !nickName.isNullOrEmpty()){
                            lifecycleScope.launch {
                                createUser(username,pwd,nickName)
                            }
                        }else{
                            binding.root.showSnackbar(message = "Please fill all fields.")
                        }

                    }

                }else{
                    showToast("Password and Confirm Password should matching..")
                }
            })
        }



    }


    private suspend fun createUser(userName: String?, password: String?, nickName: String){
        val registerUserRequestBody = RegisterUserRequestBody(username = userName, password = password, nickname = nickName)
        try {
            val response = RetrofitClient.getAgoraService(this)?.registerUser(registerUserRequestBody)
            if(response?.isSuccessful == true) {
                binding.root.showSnackbar(response.message().toString())
                finish()
            }else{
                loginToChat(userName.toString(),password.toString())
            }
        }catch (e:Exception){
            binding.root.showSnackbar(e.message.toString())
            loginToChat(userName.toString(),password.toString())

        }


    }




    private fun setUpChatClient(){
        try {
            val optins = ChatOptions()
            optins.appKey = appKey

            chatClient = ChatClient.getInstance()
            chatClient?.init(this,optins)
            chatClient?.setDebugMode(true)

            setListeners()
        }catch (e:Exception){
            showToast(e.message)
        }
    }

    private fun setListeners(){
        chatClient?.chatManager()?.addMessageListener {

        }

        chatClient?.addConnectionListener(object : ConnectionListener {
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


    fun loginToChat(user : String, pwd : String){
        chatClient?.login(user,pwd,object : CallBack {
            override fun onSuccess() {
                showToast(message = "Login onSuccess")

            }
            override fun onError(code: Int, error: String?) {
                showToast(message = "Login onError $error")
            }

            override fun onProgress(progress: Int, status: String?) {
                super.onProgress(progress, status)
                showToast(message = "Login onError ")
            }
        })
    }



}
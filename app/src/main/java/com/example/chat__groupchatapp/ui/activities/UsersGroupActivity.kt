package com.example.chat__groupchatapp.ui.activities

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat__groupchatapp.AgoraChatHelper
import com.example.chat__groupchatapp.AgoraTokenUtils.RtcTokenBuilder2
import com.example.chat__groupchatapp.R
import com.example.chat__groupchatapp.Utils.MGroupChangeListener
import com.example.chat__groupchatapp.Utils.TokenBuilder
import com.example.chat__groupchatapp.Utils.Widgets.BounceButton
import com.example.chat__groupchatapp.Utils.showSnackbar
import com.example.chat__groupchatapp.Utils.showToast
import com.example.chat__groupchatapp.data.remote.RetrofitClient
import com.example.chat__groupchatapp.data.remote.model.FirebaseNotificationBody
import com.example.chat__groupchatapp.data.remote.model.Notification
import com.example.chat__groupchatapp.data.remote.model.NotificationData
import com.example.chat__groupchatapp.data.remote.model.group.createUser.request.CreateGroupRequestBody
import com.example.chat__groupchatapp.databinding.ActivityUsersGroupBinding
import com.example.chat__groupchatapp.ui.adapter.GroupsAdapter
import com.example.chat__groupchatapp.ui.adapter.UsersAdapter
import com.example.chat__groupchatapp.ui.dialogs.CreateChatGroupDialog
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailabilityLight
import com.google.firebase.messaging.FirebaseMessaging
import io.agora.CallBack
import io.agora.chat.ChatClient
import io.agora.chat.ChatOptions
import io.agora.chat.Conversation
//import io.agora.chat.callkit.EaseCallKit
//import io.agora.chat.callkit.bean.EaseCallUserInfo
//import io.agora.chat.callkit.general.EaseCallEndReason
//import io.agora.chat.callkit.general.EaseCallError
//import io.agora.chat.callkit.general.EaseCallKitConfig
//import io.agora.chat.callkit.general.EaseCallType
//import io.agora.chat.callkit.listener.EaseCallGetUserAccountCallback
//import io.agora.chat.callkit.listener.EaseCallKitListener
//import io.agora.chat.callkit.listener.EaseCallKitTokenCallback
import io.agora.chat.uikit.EaseUIKit
import io.agora.push.PushConfig
import kotlinx.coroutines.launch
import org.json.JSONObject

class UsersGroupActivity : AppCompatActivity() {

    private var channel: String? = null
    lateinit var binding : ActivityUsersGroupBinding
    private var agoraChatHelper : AgoraChatHelper? = null

    private val usersAdapter : UsersAdapter by lazy {
        UsersAdapter(){userEntity ->
           // val intent = Intent(this,ChatActivity::class.java)
            val intent = Intent(this,AgoraChatUIActivity::class.java)
            intent.putExtra("user",userEntity)
            intent.putExtra("chat_type",Conversation.ConversationType.Chat.toString())
            startActivity(intent)
        }
    }
    private val groupAdapter : GroupsAdapter by lazy {
        GroupsAdapter(){group ->

          //  val intent = Intent(this,ChatActivity::class.java)
              val intent = Intent(this,AgoraChatUIActivity::class.java)
            intent.putExtra("group_Id",group.groupId)
            intent.putExtra("group_Name",group.groupName)
            intent.putExtra("group_description",group.description)
            intent.putExtra("group_owner",group.owner)
            intent.putExtra("chat_type",Conversation.ConversationType.GroupChat.toString())

            startActivity(intent)
        }
    }
    var currentUser : String? = null

    private val mGroupChangeListener = MGroupChangeListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpClickListeners()


        initChatUISDK()
       // initAgoraCallKitSdk()
        initFcmWithChatClient()


       // createNotification()

        agoraChatHelper = AgoraChatHelper()
        agoraChatHelper?.setUpChatClient(this)
        listenGroupChange()
        setUpRecyclerView()

        binding.logoutBtn.setContent {
            BounceButton(buttonText = "Logout") {
                logoutChat()
            }
        }


         currentUser = agoraChatHelper?.getCurrentUser().toString()
        binding.UsernameTextView.text = currentUser.toString()



        lifecycleScope.launch {

            try {
               val response =  RetrofitClient.getAgoraService(this@UsersGroupActivity)?.getUsers( limit = "5")

                if(response?.isSuccessful == true){
                   response?.body()?.entities?.let {
                       (it as MutableList).removeIf {
                           it?.username?.lowercase() == currentUser.toString().lowercase()
                       }

                       usersAdapter.submitList(it)
                   }
                }else{

                }

            }catch (e:Exception){
                showToast(e.message.toString())
            }
        }

        lifecycleScope.launch {
            val groupList = agoraChatHelper?.getJoinedGroups()
            Log.d("kjkjnknk",groupList?.size.toString())
            groupAdapter.submitList(groupList)
        }

       // EaseCallKit.getInstance().setCallKitListener(easeCallKitListener)


    }
    fun setUpRecyclerView(){
        binding.usersRecyclerView.adapter = usersAdapter
        binding.usersRecyclerView.layoutManager = LinearLayoutManager(this)

        binding.groupsRecyclerView.adapter = groupAdapter
        binding.groupsRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun logoutChat(){
        binding.progressBar.visibility = View.VISIBLE
        agoraChatHelper?.logout(onLogoutSuccess = {
            runOnUiThread {
                binding.progressBar.visibility = View.GONE
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }, onLogoutError = {
            runOnUiThread {
                binding.progressBar.visibility = View.GONE
                showToast(it)
            }
        })
    }

   private  fun setUpClickListeners(){

       binding.createGroup.setOnClickListener {
        //   agoraChatHelper?.createChatGroup("kdkcncnd",)

           val membersList = usersAdapter.currentList

           CreateChatGroupDialog(currentUser.toString() , membersList = membersList) { grpName, grpOwner, grpDesc , selectedMembersList ->

               selectedMembersList?.let {
                   createGroup(grpName, grpOwner, grpDesc , selectedMembersList)
               }

           }.show(supportFragmentManager,CreateChatGroupDialog.TAG)

       }
    }

    private fun listenGroupChange(){
     agoraChatHelper?.setGroupChangeListener(mGroupChangeListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        agoraChatHelper?.removeGroupChangeListener(mGroupChangeListener)
    }

    fun createGroup(grpName: String?, grpOwner: String?, grpDesc: String?, selectedMembersList: ArrayList<String>)
    {
        val createGroupRequestBody = CreateGroupRequestBody(
            description=  grpDesc.toString(),
            groupname= grpName.toString(),
            maxusers = 100,
            members = selectedMembersList.toList(),
            owner = grpOwner.toString(),
            public = true )

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.getAgoraService(this@UsersGroupActivity)?.createGroup(createGroupRequestBody = createGroupRequestBody)

                if (response?.isSuccessful == true) {
                    binding.root.showSnackbar(message = "Group Created")
                } else {
                    binding.root.showSnackbar(message = "${response?.code()} error.")
                }
            }catch (e:Exception){
                binding.root.showSnackbar(message = e.message.toString())
            }
        }
    }

    fun createNotification(){
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(getString(R.string.default_notification_channel_id),"Channel Name", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val fullScreenIntent = Intent(this,LoginActivity::class.java)
       // fullScreenIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        val fullScreenPendingIntent = PendingIntent.getActivity(this,0,fullScreenIntent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this,getString(R.string.default_notification_channel_id))
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("New message.")
            .setContentText("Hello")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setFullScreenIntent(fullScreenPendingIntent,true)


        notificationManager.notify("FullScreen",10,notification.build())
    }

    var fcmSenderId  = "264683372480"


    @SuppressLint("SuspiciousIndentation")
    private fun initFcmWithChatClient(){

        val chatOptions = ChatOptions()
        chatOptions.appKey = getString(R.string.APP_KEY)

        val pushConfigBuilder =  PushConfig.Builder(applicationContext)
        pushConfigBuilder.enableFCM(fcmSenderId)

        chatOptions.pushConfig = pushConfigBuilder.build()

        ChatClient.getInstance().init(applicationContext,chatOptions)


        val isAvaialble =   GoogleApiAvailabilityLight.getInstance().isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS
        Log.d("fvknvjfnv",isAvaialble.toString())

        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if(it.isSuccessful){
                Log.d("fbkgmnmnbkgmng",it.result.toString())
                ChatClient.getInstance().sendFCMTokenToServer(it.result)

               val fcmBody = FirebaseNotificationBody(to = it.result, data = NotificationData(hello = "HELOO",alert = "TESTING MESSAGE",chatType = "CHATtYPE"), notification = Notification("title","content"))

                lifecycleScope.launch {
                  //  RetrofitClient.getAgoraService(this@UsersGroupActivity)?.sendFCMNotification(fcmBody)
                }
                ChatClient.getInstance().pushManager().bindDeviceToken(fcmSenderId,it.result,object : CallBack{
                    override fun onSuccess() {
                        Log.d("gfkbngjbng","succes")
                    }

                    override fun onError(code: Int, error: String?) {
                        Log.d("gfkbngjbng","succes")
                    }

                    override fun onProgress(progress: Int, status: String?) {
                        super.onProgress(progress, status)
                        Log.d("gfkbngjbng","succes")
                    }
                })
            }else{
                Log.d("fvknfkvnfv",it.exception?.message.toString() + " error")
            }
        }


    }
    fun initChatUISDK(){
        val  chatoptions = ChatOptions()
        chatoptions.appKey = getString(R.string.APP_KEY)
        chatoptions.requireDeliveryAck = true
        chatoptions.autoLogin = true
        EaseUIKit.getInstance().init(this,chatoptions)
    }

/*
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
*/

    

/*
    val easeCallKitListener = object : EaseCallKitListener {
        override fun onInviteUsers(
            callType: EaseCallType?,
            existMembers: Array<out String>?,
            ext: JSONObject?,
        ) {
            Log.d("fvmkf3232mv","onInviteIsers")
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

            channel = channelName

            val token = TokenBuilder.getRtcTokenOfUid(this@UsersGroupActivity,userId?.toInt() ?: 0,channelName.toString(),
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
            Log.d("fkbmkbmf",channel.toString())
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
*/

    
    


}
package com.example.chat__groupchatapp.ui.activities

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
import com.example.chat__groupchatapp.R
import com.example.chat__groupchatapp.Utils.MGroupChangeListener
import com.example.chat__groupchatapp.Utils.Widgets.BounceButton
import com.example.chat__groupchatapp.Utils.showSnackbar
import com.example.chat__groupchatapp.Utils.showToast
import com.example.chat__groupchatapp.data.remote.RetrofitClient
import com.example.chat__groupchatapp.data.remote.model.group.createUser.request.CreateGroupRequestBody
import com.example.chat__groupchatapp.databinding.ActivityUsersGroupBinding
import com.example.chat__groupchatapp.ui.adapter.GroupsAdapter
import com.example.chat__groupchatapp.ui.adapter.UsersAdapter
import com.example.chat__groupchatapp.ui.dialogs.CreateChatGroupDialog
import io.agora.chat.Conversation
import kotlinx.coroutines.launch

class UsersGroupActivity : AppCompatActivity() {

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



}
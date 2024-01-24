package com.example.chat__groupchatapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.PopupWindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat__groupchatapp.AgoraChatHelper
import com.example.chat__groupchatapp.AgoraTokenUtils.ChatTokenBuilder2
import com.example.chat__groupchatapp.R
import com.example.chat__groupchatapp.Utils.Widgets.BounceButton
import com.example.chat__groupchatapp.Utils.bearerToken
import com.example.chat__groupchatapp.Utils.getExpiryInSeconds
import com.example.chat__groupchatapp.Utils.showToast
import com.example.chat__groupchatapp.data.remote.RetrofitClient
import com.example.chat__groupchatapp.databinding.ActivityUsersGroupBinding
import com.example.chat__groupchatapp.databinding.ItemsLayoutsBinding
import com.example.chat__groupchatapp.databinding.ItemsSpinnerItemBinding
import com.example.chat__groupchatapp.ui.adapter.GroupsAdapter
import com.example.chat__groupchatapp.ui.adapter.UsersAdapter
import com.example.chat__groupchatapp.ui.dialogs.CreateChatGroupDialog
import kotlinx.coroutines.launch

class UsersGroupActivity : AppCompatActivity() {

    lateinit var binding : ActivityUsersGroupBinding
    private var agoraChatHelper : AgoraChatHelper? = null

    private val usersAdapter : UsersAdapter by lazy {
        UsersAdapter()
    }
    private val groupAdapter : GroupsAdapter by lazy {
        GroupsAdapter()
    }
    var currentUser : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpClickListeners()

        agoraChatHelper = AgoraChatHelper()
        agoraChatHelper?.setUpChatClient(this)
        setUpRecyclerView()

        binding.logoutBtn.setContent {
            BounceButton(buttonText = "Logout") {
                logoutChat()
            }
        }


         currentUser = agoraChatHelper?.getCurrentUser().toString()
        binding.UsernameTextView.text = currentUser.toString()



        lifecycleScope.launch {
            val appId = getString(R.string.APP_ID)
            val appCertificate = getString(R.string.APP_CERTIFICATE)
            val chatAppToken = ChatTokenBuilder2()
                .buildAppToken(appId,appCertificate, getExpiryInSeconds(5))

            try {
               val response =  RetrofitClient.getAgoraService()?.getUsers(chatAppToken = chatAppToken.bearerToken(), limit = "5")

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
                   agoraChatHelper?.createChatGroup(grpName.toString(),grpDesc.toString(), it.toTypedArray())
               }

           }
               .show(supportFragmentManager,CreateChatGroupDialog.TAG)

       }
    }

}
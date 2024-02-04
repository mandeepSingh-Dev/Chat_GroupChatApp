package com.example.chat__groupchatapp.ui.activities

import com.example.chat__groupchatapp.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.Ringtone
import android.media.RingtoneManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agorademoapps.Util.RtcEventHandler
import com.example.chat__groupchatapp.AgoraTokenUtils.RtcTokenBuilder2
import com.example.chat__groupchatapp.Utils.TokenBuilder
import com.example.chat__groupchatapp.Utils.invisible
import com.example.chat__groupchatapp.Utils.showToast
import com.example.chat__groupchatapp.Utils.visible
import com.example.chat__groupchatapp.databinding.ActivityGroupVideoCallBinding
import com.example.chat__groupchatapp.databinding.ActivityVideoBinding
import com.example.chat__groupchatapp.ui.adapter.GroupVideoSurfaceViewAdapter
import io.agora.chat.ChatClient
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.video.VideoCanvas
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.random.nextInt




class GroupVideoCallActivity : AppCompatActivity() {

    lateinit var binding : ActivityGroupVideoCallBinding

    var rtcEngine : RtcEngine? = null

    var uId = 1
    var isJoined = false
    var token = ""


    val REQUESTED_PERMISSIONS = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CAMERA
    )

    val groupVideoSurfaceViewAdapter : GroupVideoSurfaceViewAdapter by lazy {
        GroupVideoSurfaceViewAdapter()
    }

    var channelName : String? = ""
    var appId : String? = ""


    var userId : String = ""
    var isComingCall : String? = "false"
    var callType : String? = ""
    var voice_or_Video : String? = ""
    var caller_Id: String? = ""
    var call_or_chat : String? = ""

    var nickName: String? = ""
    var groupId: String? = ""
    var groupName: String? = ""
    var groupOwner: String? = ""
    var groupDescription: String? = ""
    var membersList : ArrayList<String>? = arrayListOf()

    private var ringtone: Ringtone? = null


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupVideoCallBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appId = getString(R.string.APP_ID)

        try {
            uId = ChatClient.getInstance().currentUser.toInt() ?: 0
        }catch (e:Exception){
            val sp = getSharedPreferences(com.example.chat__groupchatapp.Utils.Constants.sharedPrefName, MODE_PRIVATE)
            uId = sp.getString(com.example.chat__groupchatapp.Utils.Constants.user,"")?.toInt() ?: 0
        }


        isComingCall = intent.getStringExtra(MConstants.IS_INCOMING_CALL)
        userId = intent.getStringExtra(MConstants.TARGET_USER_ID) ?: ""
        isComingCall = intent.getStringExtra(MConstants.IS_INCOMING_CALL)
        channelName = intent.getStringExtra(MConstants.CHANNEL_NAME) ?: ""
        callType = intent.getStringExtra(MConstants.CALL_TYPE)
        voice_or_Video = intent.getStringExtra(MConstants.VOICE_OR_VIDEO)
        caller_Id = intent.getStringExtra(MConstants.CALLER_ID)
        call_or_chat = intent.getStringExtra(MConstants.CALL_OR_CHAT)

        nickName = intent.getStringExtra(MConstants.NICKNAME)
        groupId = intent.getStringExtra(MConstants.GROUP_ID)
        groupName = intent.getStringExtra(MConstants.GROUP_NAME)
        groupOwner = intent.getStringExtra(MConstants.GROUP_OWNER)
        groupDescription = intent.getStringExtra(MConstants.GROUP_DESCRIPTION)
        membersList = intent.getStringArrayListExtra(MConstants.GROUP_MEMBERS_LIST)

        token = TokenBuilder.getRtcTokenOfUid(this,uId,channelName.toString(),RtcTokenBuilder2.Role.ROLE_PUBLISHER)

        binding.videoInfo.setText( "uid: $uId ChannelName:$channelName" )

        if (!checkSelfPermission()) {
            permissionsLauncher.launch(REQUESTED_PERMISSIONS)
        }else{
            setUpAgoraEngine()
        }

        if(isComingCall == "true"){
            val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            ringtone = RingtoneManager.getRingtone(this,ringtoneUri)
            ringtone?.play()
        }

        if(isComingCall == "false"){
            binding.endCallButton.visible()
            binding.joinButton.invisible()

        }else{
            binding.endCallButton.visible()
            binding.joinButton.visible()
        }



        setUpRecyclerView()

        setClickListener()
    }

    fun setClickListener(){
        binding.joinButton.setOnClickListener {
            joinChannel()
            ringtone?.stop()
        }
        binding.endCallButton.setOnClickListener {
            leaveChannel()
        }
        binding.speakerButton.setOnClickListener {
            rtcEngine?.isSpeakerphoneEnabled?.let {
                rtcEngine?.setEnableSpeakerphone(!it)
            }
        }

    }

    fun checkSelfPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, REQUESTED_PERMISSIONS[0]) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, REQUESTED_PERMISSIONS[1]) == PackageManager.PERMISSION_GRANTED
    }

    private val permissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
        val recordAudioPermission = it.get(REQUESTED_PERMISSIONS[0]) ?: false
        val cameraPermission = it.get(REQUESTED_PERMISSIONS[1]) ?: false

        if(!recordAudioPermission && !cameraPermission){
            showToast("Please allow camera and audio permission.")
        }else if(!recordAudioPermission){
            showToast("Please allow audio permission.")
        }else if(!cameraPermission){
            showToast("Please allow camera permission.")
        }else{
            setUpAgoraEngine()
        }
    }

    private fun setUpAgoraEngine(){
        try {

            rtcEngine = RtcEngine.create(this,appId,rtcEventHandler)

            rtcEngine?.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION)
            rtcEngine?.enableVideo()

            if(isComingCall == "false") {
                joinChannel()
            }

        }catch (e: Exception){
        }
    }

    var rtcEventHandler = RtcEventHandler(
    mOnUserJoined = {
        Log.d("fvklkfnbkf","onUserJoined  $it")
        isJoined = true
        setUpRemoteVideo(it)

    },
    mOnJoinChannelSuccesss = {e,r,t ->},
    mOnUserOffline = {uid ->
        Log.d("fvklkfnbkf","onUserOffline")
        removeItemFromGroupRV(uid)
    })

    private fun joinChannel(){
        if(!checkSelfPermission()){
            showToast("Permissions are not granted!")
            return
        }

        if(rtcEngine == null) setUpAgoraEngine()

        setUpLocalVideo()
        rtcEngine?.startPreview()


        val channelOptions = ChannelMediaOptions()
        channelOptions.channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION


        /**To allow only group members UID to join the video. */
        // rtcEngine?.setSubscribeVideoAllowlist()


        rtcEngine?.joinChannel(token,channelName,uId,channelOptions)


        var sharedPreference = getSharedPreferences("Local_Stroge", Context.MODE_PRIVATE)
        //   anotherDeviceToken = sharedPreference.getString(com.example.agorademoapps.Constants.ANOTHER_DEVICE_TOKEN_KEY,"").toString()

        // val remoteMessage = RemoteMessage.Builder(anotherDeviceToken).build()
        //  FirebaseMessaging.getInstance().send(remoteMessage)

    }

    private fun setUpRemoteVideo(remoteUid : Int){

        val remoteSurfaceView = SurfaceView(this)
        remoteSurfaceView.setZOrderMediaOverlay(true)

        val videoCanvas = VideoCanvas(remoteSurfaceView, VideoCanvas.RENDER_MODE_FIT,remoteUid)

        rtcEngine?.setupRemoteVideo(videoCanvas)
        runOnUiThread {
            val surfaceViewItem = SurfaceViewItem(remoteSurfaceView, uid = remoteUid)
            addItemToGroupRV(surfaceViewItem)
          //  binding.remoteVideoFrameCardView.visibility = View.VISIBLE
          //  binding.remoteVideoFrame.addView(remoteSurfaceView)

            binding.videoInfo.append("User Joined : Remote Uid - $remoteUid")
        }
    }

    private fun setUpLocalVideo(){
        val localSurfaceView = SurfaceView(this)
        val videoCanvas = VideoCanvas(localSurfaceView,VideoCanvas.RENDER_MODE_HIDDEN,uId)
        rtcEngine?.setupLocalVideo(videoCanvas)

       val surfaceViewItem = SurfaceViewItem(localSurfaceView, uid = uId)

        addItemToGroupRV(surfaceViewItem)

      //  binding.localVideoFrame.addView(localSurfaceView)
    }

    fun leaveChannel(){
        try {
            ringtone?.stop()
            rtcEngine?.leaveChannel()
            isJoined = false
            destroyRTCEngine()
        }catch (e:Exception){}
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        ringtone?.audioAttributes
        ringtone?.stop()
        leaveChannel()
    }

    fun destroyRTCEngine(){
        RtcEngine.destroy()
        rtcEngine = null
    }

    fun setUpRecyclerView(){
        binding.groupVideoRecyclerView.adapter = groupVideoSurfaceViewAdapter
        binding.groupVideoRecyclerView.layoutManager = GridLayoutManager(this,2)
    }

    fun addItemToGroupRV(surfaceViewItem: SurfaceViewItem){
      val mList =  groupVideoSurfaceViewAdapter.currentList.toMutableList()
        mList.add(surfaceViewItem)

        groupVideoSurfaceViewAdapter.submitList(mList)
    }

    private fun removeItemFromGroupRV(uid : Int){
        val mList =  groupVideoSurfaceViewAdapter.currentList.toMutableList()
        mList.removeIf { it.uid == uid }
        groupVideoSurfaceViewAdapter.submitList(mList)
    }

    //When user reject the call then from FCM service this activity class start by startActivity with SINGLE_TOP that will deliver newIntent not created newInstance of activity.
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val reject = intent?.getStringExtra(MConstants.REJECT_CALL_ACTION).toString()
        if(reject == MConstants.REJECT_CALL_ACTION_VALUE)
        {
            finish()
        }
    }

}

data class SurfaceViewItem(val surfaceView: SurfaceView, val isClosed : Boolean = false, val uid : Int)
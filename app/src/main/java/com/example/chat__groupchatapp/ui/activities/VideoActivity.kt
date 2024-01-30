package com.example.chat__groupchatapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telecom.Connection
import android.telecom.Connection.PROPERTY_SELF_MANAGED
import android.telecom.PhoneAccount
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.MyConnectionService
import com.example.agorademoapps.Util.RtcEventHandler
import com.example.chat__groupchatapp.AgoraTokenUtils.RtcTokenBuilder2
import com.example.chat__groupchatapp.Utils.TokenBuilder
import com.example.chat__groupchatapp.Utils.showToast
import com.example.chat__groupchatapp.databinding.ActivityVideoBinding
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.video.VideoCanvas
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.random.nextInt




class VideoActivity : AppCompatActivity() {

    lateinit var binding : ActivityVideoBinding

    var rtcEngine : RtcEngine? = null
    private val appId = "3b7f0443aa0f4ad4ab145c3044729318"
    private val channelName = "Test-Channel"
    var uId = 1
    var remoteuId = 1
    var isJoined = false
    var token = "007eJxTYLhY2We5+Picnt4HBQ0FslXJe1mK/Y4wfLT6IqXoWBfLcF+BwTjJPM3AxMQ4MdEgzSQxxSQxydDENNkYKGRuZGlsaPEmbnlqQyAjw/RfUoyMDBAI4vMwhKQWl+g6ZyTm5aXmMDAAADsEIc0="

    var anotherDeviceToken = "fw978bf8Tl6l1dHBVFTwP4:APA91bFRX5P6yeOdtNo0S6LVLCFEkn3WHM55aeyXUqfK0XZsxGpsoqKf0ScKvqcVMkxVxIALGmAnWKfI07w5z4m0ECIhLPPiFKPmt9uTMXOl7Se6cMimfJbh1BoIMLMgQGnwDU7bcHo2"

    val REQUESTED_PERMISSIONS = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CAMERA
    )



    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        uId = Random.nextInt(IntRange(1,1000))

        showToast(intent.getStringExtra("local_uid").toString())

        binding.videoInfo.setText( "uid: $uId ChannelName:$channelName" )
        token = TokenBuilder.getRtcTokenOfUid(this,uId,channelName,RtcTokenBuilder2.Role.ROLE_PUBLISHER)

        if (!checkSelfPermission()) {
            permissionsLauncher.launch(REQUESTED_PERMISSIONS)
        }else{
            setUpAgoraEngine()
        }

        binding.joinLeaveBtn.setOnClickListener {
            Log.d("fvlmvmkf",isJoined.toString())
            if(isJoined)
                leaveChannel()
            else
                joinChannel(channelName,token)
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

    fun setUpAgoraEngine(){
        try {

            rtcEngine = RtcEngine.create(this,appId, RtcEventHandler(
                mOnUserJoined = {
                Log.d("fvklkfnbkf","onUserJoined  $it")
                isJoined = true

                setUpRemoteVideo(it)

            }, mOnJoinChannelSuccesss = {e,r,t-> } , mOnUserOffline = {
                Log.d("fvklkfnbkf","onUserOffline")
                leaveChannel()
            }))

            rtcEngine?.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION)
            rtcEngine?.enableVideo()
        }catch (e: Exception){
        }
    }

    fun joinChannel(channelName : String, token : String){
        if(!checkSelfPermission()){
            showToast("Permissions are not granted!")
            return
        }

        if(rtcEngine == null) setUpAgoraEngine()

        setUpLocalVideo()
        rtcEngine?.startPreview()


        val channelOptions = ChannelMediaOptions()
        channelOptions.channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION

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
            binding.remoteVideoFrameCardView.visibility = View.VISIBLE
            binding.remoteVideoFrame.addView(remoteSurfaceView)

            binding.videoInfo.append("User Joined : Remote Uid - $remoteUid")
            binding.joinLeaveBtn.text = "Leave"

        }
    }

    private fun setUpLocalVideo(){
        val localSurfaceView = SurfaceView(this)
        val videoCanvas = VideoCanvas(localSurfaceView,VideoCanvas.RENDER_MODE_HIDDEN,uId)
        rtcEngine?.setupLocalVideo(videoCanvas)

        binding.localVideoFrame.addView(localSurfaceView)
    }

    fun leaveChannel(){

        CoroutineScope(Dispatchers.Main).launch {
            if (isJoined) {
                rtcEngine?.leaveChannel()
                isJoined = false
                binding.joinLeaveBtn.text = "join"
                binding.remoteVideoFrameCardView.visibility = View.GONE

                // destroyRTCEngine()

            } else {

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        leaveChannel()
        destroyRTCEngine()
    }

    fun destroyRTCEngine(){
        RtcEngine.destroy()
        rtcEngine = null
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    fun buildPhoneAccount(){
        val telecomManager = getSystemService(TELECOM_SERVICE) as TelecomManager
        val componentName = ComponentName(this, MyConnectionService::class.java)
        val phoneAccountHandle = PhoneAccountHandle(componentName, "AGORA_APP_TAG")
        val phoneAccount = telecomManager.getPhoneAccount(phoneAccountHandle)
        telecomManager.registerPhoneAccount(phoneAccount)


       val connection = MyConnectionClass().let {
            it.connectionProperties = PROPERTY_SELF_MANAGED
            it.audioModeIsVoip = true
           it
        }

        connection.setInitializing()
        connection.setRinging()




    }
}

class MyConnectionClass : Connection(){

}
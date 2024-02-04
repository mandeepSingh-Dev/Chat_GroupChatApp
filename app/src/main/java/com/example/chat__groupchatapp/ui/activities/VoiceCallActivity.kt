package com.example.chat__groupchatapp.ui.activities

import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import com.example.agorademoapps.Util.RtcEventHandler
import com.example.chat__groupchatapp.AgoraTokenUtils.RtcTokenBuilder2
import com.example.chat__groupchatapp.ChatCallUtils
import com.example.chat__groupchatapp.R
import com.example.chat__groupchatapp.Utils.TokenBuilder
import com.example.chat__groupchatapp.Utils.invisible
import com.example.chat__groupchatapp.Utils.visible
import com.example.chat__groupchatapp.databinding.ActivityVoiceCallBinding
import io.agora.chat.ChatClient
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.RtcEngine


class VoiceCallActivity : AppCompatActivity() {

    private var ringtone: Ringtone? = null
    lateinit var binding : ActivityVoiceCallBinding

    var rtcEngine : RtcEngine? = null
    var appId = ""
    var channelName = "Channel_Call"
    var localUid = 1
    var remoteUid = 0
    var isJoined = false
    var tempToken = ""

    val PERMISSION_REQ_ID = 22
    val REQUESTED_PERMISSIONS = arrayOf(
        android.Manifest.permission.RECORD_AUDIO
    )

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVoiceCallBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appId = getString(R.string.APP_ID)


        try {
            localUid = ChatClient.getInstance().currentUser.toInt() ?: 0
        }catch (e:Exception){
            val sp = getSharedPreferences(com.example.chat__groupchatapp.Utils.Constants.sharedPrefName, MODE_PRIVATE)
            localUid = sp.getString(com.example.chat__groupchatapp.Utils.Constants.user,"")?.toInt() ?: 0
        }
        userId = intent.getStringExtra(MConstants.TARGET_USER_ID) ?: ""
        channelName = intent.getStringExtra(MConstants.CHANNEL_NAME) ?: ""
        isComingCall = intent.getStringExtra(MConstants.IS_INCOMING_CALL)
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

        if(isComingCall == "true"){
        val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        ringtone = RingtoneManager.getRingtone(this,ringtoneUri)
        ringtone?.play()
    }

        if(callType == MConstants.SINGLE_CALL_TYPE_VALUE){
            if(isComingCall == "true"){
                binding.usersNameTextView.text = caller_Id
            }else{
                binding.usersNameTextView.text = userId.toString()
            }
        }else{
                binding.usersNameTextView.text = groupName
        }

        tempToken = TokenBuilder.getRtcTokenOfUid(this,localUid,channelName,RtcTokenBuilder2.Role.ROLE_PUBLISHER)

        setUpRtcEngine()
        setClickListeners()

        if(isComingCall == "false"){
            binding.endCallButton.visible()
            binding.joinButton.invisible()

        }else{
            binding.endCallButton.visible()
            binding.joinButton.visible()
        }
    }

    fun setClickListeners(){

        binding.endCallButton.setOnClickListener {
            // ringtone?.stop()
            leaveChannel()

            if (callType == MConstants.SINGLE_CALL_TYPE_VALUE) {
                ChatCallUtils.sendRejectCallMessage(
                    this,
                    callType = callType.toString(),
                    voice_or_video = MConstants.VOICE_CALL_VALUE,
                    toUserId = userId.toString(),
                    channelName,
                    groupId,
                    groupName,
                    groupOwner,
                    groupDescription
                )
            }
            //If Call from Group Chat
            else {
                //Sending Invitations to all members one by one.
                Log.d("fvlmkvbfm", "2 DFDF")
                Log.d("fvjkvfnkv", membersList?.size.toString())
                membersList?.forEach { userId ->
                    ChatCallUtils.sendRejectCallMessage(
                        this,
                        callType = callType.toString(),
                        voice_or_video = MConstants.VOICE_CALL_VALUE,
                        toUserId = userId.toString(),
                        channelName,
                        groupId,
                        groupName,
                        groupOwner,
                        groupDescription
                    )
                }
            }

        }
        binding.joinButton.setOnClickListener {
            ringtone?.stop()
            joinChannel()
        }
        binding.speakerButton.setOnClickListener {
            rtcEngine?.isSpeakerphoneEnabled?.let { it1 -> rtcEngine?.setEnableSpeakerphone(!it1) }
        }
    }

    private fun setUpRtcEngine(){
        try {
            rtcEngine = RtcEngine.create(this,appId,rtcEventHandler)

            if(isComingCall == "false") joinChannel()

        }catch (e:Exception){
            Log.d("fvmfkvfv",e.message.toString())
        }

    }

    val rtcEventHandler = RtcEventHandler(
    mOnUserJoined = {remoteUidd ->
        runOnUiThread {
            var count = 0L
            object : CountDownTimer(Long.MAX_VALUE, 1000L) {
                override fun onTick(millisUntilFinished: Long) {
                    count += 1000L
                    Log.d("fvkmfkvf", (count / 1000).toString())

                    var minutes = (count / 1000) / 60
                    var seconds = (count / 1000) % 60

                    Log.d("fvkmnvkf", "$minutes $seconds ")
                    val minutesStr = if (minutes < 10) "0$minutes" else "$minutes"
                    val secondsStr = if (seconds < 10) "0$seconds" else "$seconds"
                    binding.callStatusTextView.text = "$minutesStr:$secondsStr"

                }

                override fun onFinish() {
                }
            }.start()
            remoteUid = remoteUidd

            if (callType == MConstants.GROUP_CALL_TYPE_VALUE){
                binding.usersNameTextView.append((if (nickName?.isEmpty() == true || nickName.isNullOrEmpty()) userId else nickName)+"\n")
            }
        }

    },
    mOnUserOffline = {
        //If call type is SINGLE only then on remote UserOffline leave channel.
        if(callType == MConstants.SINGLE_CALL_TYPE_VALUE) {
            leaveChannel()
        }
    },
    mOnJoinChannelSuccesss = {channel,uid,e->
        isJoined = true
        localUid = uid

        //If not InComing Call then send Call Invitation otherwise not.
        if(isComingCall == "false") {
            //If Call from Single Chat
            if (callType == MConstants.SINGLE_CALL_TYPE_VALUE) {
                Log.d("fvlmkvbfm","1 FDFD")
                ChatCallUtils.sendCallInvitationMessage(
                    this,
                    callType = callType.toString(),
                    voice_or_video = MConstants.VOICE_CALL_VALUE,
                    toUserId = userId.toString(),
                    channelName,
                    groupId,
                    groupName,
                    groupOwner,
                    groupDescription
                )
            }
            //If Call from Group Chat
            else {
                //Sending Invitations to all members one by one.
                Log.d("fvlmkvbfm","2 DFDF")
                Log.d("fvjkvfnkv",membersList?.size.toString())
                membersList?.forEach { userId ->
                    ChatCallUtils.sendCallInvitationMessage(this, callType = callType.toString(), voice_or_video = MConstants.VOICE_CALL_VALUE, toUserId = userId.toString(), channelName,groupId,groupName,groupOwner,groupDescription)
                }
            }
        }


    })

    private fun joinChannel(){

      //  rtcEngine?.setSubscribeAudioAllowlist(intArrayOf(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30))

        val channelMediaOptions = ChannelMediaOptions()
        channelMediaOptions.channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION
        channelMediaOptions.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER

        rtcEngine?.setDefaultAudioRoutetoSpeakerphone(false)
        rtcEngine?.setEnableSpeakerphone(false)


        Log.d("fvjbjvfvf",localUid.toString())

        rtcEngine?.joinChannel(tempToken, channelName,localUid,channelMediaOptions)
        Log.d("dknkdv","joinCHannel 2 ")

    }

    fun leaveChannel(){
        try {
           // ringtone?.stop()
            isJoined = false
            rtcEngine?.leaveChannel()
            destroyRTCEngine()
            finish()
        }catch (e:Exception){}
    }

    fun destroyRTCEngine(){
        try {
            RtcEngine.destroy()
            rtcEngine = null
        }catch (e:Exception){}
    }

    override fun onDestroy() {
        super.onDestroy()
        ringtone?.stop()
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
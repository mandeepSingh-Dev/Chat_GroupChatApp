package com.example.chat__groupchatapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import com.example.agorademoapps.Util.RtcEventHandler
import com.example.chat__groupchatapp.AgoraTokenUtils.RtcTokenBuilder2
import com.example.chat__groupchatapp.Utils.TokenBuilder
import com.example.chat__groupchatapp.databinding.ActivityVoiceCallBinding
import io.agora.chat.Conversation
import io.agora.chat.Conversation.ConversationType
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.RtcEngine
import kotlin.random.Random
import kotlin.random.nextInt


class VoiceCallActivity : AppCompatActivity() {

    lateinit var binding : ActivityVoiceCallBinding

    var rtcEngine : RtcEngine? = null
    var appId = "3b7f0443aa0f4ad4ab145c3044729318"
    var channelName = "Channel_Call"
    var localUid = 1
    var remoteUid = 0
    var isJoined = false
    var tempToken ="007eJxTYChP+Dzv9Y+XOessOWyfvWiMKJ6+rve8yIeHR/peuz2TfMSiwGCcZJ5mYGJinJhokGaSmGKSmGRoYppsDBQyN7I0NrQI2rYstSGQkSFwqiojIwMEgvg8DM4ZiXl5qTnxzok5OQwMAGl+JMc="

    val PERMISSION_REQ_ID = 22
    val REQUESTED_PERMISSIONS = arrayOf(
        android.Manifest.permission.RECORD_AUDIO
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVoiceCallBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val chatType = intent.getStringExtra("chat_type")
        val name = if(chatType == ConversationType.Chat.toString()){
           val name = intent.getStringExtra("name")
            name
        }else{
            var name = intent.getStringExtra("name") + ": "
            val array = intent.getStringArrayExtra("membersList")
            array?.forEach {
            name += " $it"
            }
            "$name"
        }
        binding.callerNameTextView.text = name

        localUid = Random.nextInt(IntRange(1,30))
        tempToken = TokenBuilder.getRtcTokenOfUid(this,localUid,channelName,RtcTokenBuilder2.Role.ROLE_PUBLISHER)

        setUpRtcEngine()



        binding.endCallButton.setOnClickListener {
            leaveChannel()
        }

    }

    fun setUpRtcEngine(){
        try {
            rtcEngine = RtcEngine.create(this,appId, RtcEventHandler(
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
                    }

                },
                mOnUserOffline = {
                    leaveChannel()
                },
                mOnJoinChannelSuccesss = {channel,uid,e->
                    isJoined = true
                    localUid = uid
                })
            )

            joinChannel()
        }catch (e:Exception){
            Log.d("fvmfkvfv",e.message.toString())
        }

    }

    private fun joinChannel(){

        rtcEngine?.setSubscribeAudioAllowlist(intArrayOf(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30))

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
        isJoined = false
        rtcEngine?.leaveChannel()
        finish()
    }

}
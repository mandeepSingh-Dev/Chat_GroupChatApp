package com.example.chat__groupchatapp

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.chat__groupchatapp.ui.activities.MConstants
import io.agora.CallBack
import io.agora.chat.ChatClient
import io.agora.chat.ChatMessage
import io.agora.chat.TextMessageBody
import io.agora.chat.uikit.EaseUIKit
import org.json.JSONArray
import org.json.JSONObject

object ChatCallUtils {

    fun sendCallInvitationMessage(
        context: Context?,
        callType: String,
        voice_or_video: String,
        toUserId: String,
        channelName: String?,
        groupId: String?=null,
        groupName: String?=null,
        groupOwner: String?=null,
        groupDescription: String?=null
    ){
        val chatMessage = ChatMessage.createSendMessage(ChatMessage.Type.TXT)

        Log.d("fvblfmbkfm",channelName.toString())

        val callActionMessage = if(voice_or_video == MConstants.VOICE_CALL_VALUE){
            if(callType == MConstants.SINGLE_CALL_TYPE_VALUE){
                 "Voice Call"
            }else {
                "Group Voice Call"
            }
        }else{
            if(callType == MConstants.SINGLE_CALL_TYPE_VALUE){
                "Video Call"
            }else {
                "Group Video Call"
            }
        }

        val textMessage = TextMessageBody(callActionMessage)
        val pushObject = JSONObject()
        val titleArgs = JSONArray()
        val contentArgs = JSONArray()

        try{
            pushObject.put(MConstants.IS_INCOMING_CALL,"true")
            pushObject.put(MConstants.CHANNEL_NAME, channelName)
            pushObject.put(MConstants.TARGET_USER_ID,toUserId)
            pushObject.put(MConstants.CALL_TYPE,callType)
            pushObject.put(MConstants.VOICE_OR_VIDEO,voice_or_video)
            pushObject.put(MConstants.CALLER_ID, ChatClient.getInstance().currentUser)
            pushObject.put(MConstants.CALL_OR_CHAT, MConstants.CALL_VALUE)
            pushObject.put(MConstants.GROUP_ID,groupId)
            pushObject.put(MConstants.GROUP_NAME,groupName)
            pushObject.put(MConstants.GROUP_OWNER,groupOwner)
            pushObject.put(MConstants.GROUP_DESCRIPTION,groupDescription)

        }catch (e:Exception){
            Log.d("Fknbkjbg",e.message.toString())
        }

        Log.d("fvlmkvmfv",toUserId.toString())
        chatMessage.to = toUserId
        // chatMessage.to = user

        chatMessage.chatType = ChatMessage.ChatType.Chat
        chatMessage.body = textMessage

        chatMessage?.setAttribute("em_apns_ext",pushObject)
        //  chatMessage?.setAttribute("em_force_notification", true);

        chatMessage.setMessageStatusCallback(object: CallBack {
            override fun onSuccess() {
                Log.d("gbkngkbngbg","Chat Message send succefully")
            }

            override fun onError(code: Int, error: String?) {
                Log.d("fvkfvkfv",error.toString())
                Log.d("gbkngkbngbg","Chat Message onError ")
            }

            override fun onProgress(progress: Int, status: String?) {
                super.onProgress(progress, status)
                Log.d("gbkngkbngbg","onProgress")
            }
        })

        ChatClient.getInstance().chatManager().sendMessage(chatMessage)
    }
    fun sendRejectCallMessage(
        context: Context?,
        callType: String,
        voice_or_video: String,
        toUserId: String,
        channelName: String?,
        groupId: String?=null,
        groupName: String?=null,
        groupOwner: String?=null,
        groupDescription: String?=null
    ){

        val chatMessage = ChatMessage.createSendMessage(ChatMessage.Type.TXT)

        val callActionMessage = if(voice_or_video == MConstants.VOICE_CALL_VALUE){
            if(callType == MConstants.SINGLE_CALL_TYPE_VALUE){
                "voice call closed"
            }else {
                "Group voice call closed"
            }
        }else{
            if(callType == MConstants.SINGLE_CALL_TYPE_VALUE){
                "Video call closed"
            }else {
                "Group video call closed"
            }
        }
        val textMessage = TextMessageBody(callActionMessage)
        val pushObject = JSONObject()
        val titleArgs = JSONArray()
        val contentArgs = JSONArray()

        try{
            pushObject.put(MConstants.IS_INCOMING_CALL,"true")
            pushObject.put(MConstants.CHANNEL_NAME, channelName)
            pushObject.put(MConstants.TARGET_USER_ID,toUserId)
            pushObject.put(MConstants.CALL_TYPE,callType)
            pushObject.put(MConstants.VOICE_OR_VIDEO,voice_or_video)
            pushObject.put(MConstants.CALLER_ID, ChatClient.getInstance().currentUser)
            pushObject.put(MConstants.CALL_OR_CHAT, MConstants.CALL_VALUE)
            pushObject.put(MConstants.GROUP_ID,groupId)
            pushObject.put(MConstants.GROUP_NAME,groupName)
            pushObject.put(MConstants.GROUP_OWNER,groupOwner)
            pushObject.put(MConstants.GROUP_DESCRIPTION,groupDescription)
            pushObject.put(MConstants.REJECT_CALL_ACTION,MConstants.REJECT_CALL_ACTION_VALUE)

        }catch (e:Exception){
            Log.d("Fknbkjbg",e.message.toString())
        }

        Log.d("fvlmkvmfv",toUserId.toString())
        chatMessage.to = toUserId
        // chatMessage.to = user

        chatMessage.chatType = ChatMessage.ChatType.Chat
        chatMessage.body = textMessage

        chatMessage?.setAttribute("em_apns_ext",pushObject)
        //  chatMessage?.setAttribute("em_force_notification", true);

        chatMessage.setMessageStatusCallback(object: CallBack {
            override fun onSuccess() {
                Log.d("gbkngkbngbg","Chat Message send succefully")
            }

            override fun onError(code: Int, error: String?) {
                Log.d("fvkfvkfv",error.toString())
                Log.d("gbkngkbngbg","Chat Message onError ")
            }

            override fun onProgress(progress: Int, status: String?) {
                super.onProgress(progress, status)
                Log.d("gbkngkbngbg","onProgress")
            }
        })

        ChatClient.getInstance().chatManager().sendMessage(chatMessage)
    }


}
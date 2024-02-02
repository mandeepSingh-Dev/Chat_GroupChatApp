package com.example.chat__groupchatapp.ui.fagments

import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.core.app.NotificationCompat.MessagingStyle.Message
import io.agora.chat.ChatMessage
import io.agora.chat.CmdMessageBody
import io.agora.chat.CombineMessageBody
import io.agora.chat.ImageMessageBody
import io.agora.chat.LocationMessageBody
import io.agora.chat.MessageBody
import io.agora.chat.TextMessageBody
import io.agora.chat.VideoMessageBody
import io.agora.chat.VoiceMessageBody
import io.agora.chat.uikit.chat.EaseChatFragment
import org.json.JSONArray
import org.json.JSONObject

class CustomEaseChatFragment : EaseChatFragment() {


    override fun addMsgAttrsBeforeSend(message: ChatMessage?) {
        super.addMsgAttrsBeforeSend(message)

        val pushObject = JSONObject()
        val titleArgs = JSONArray()
        val contentArgs = JSONArray()

        //If dont want to push notification to message chatType only then paas "null" to to (to is username).
      //  message?.to = "null"

        try{
            //This is custom key added in data object.
            pushObject.put("alert", message?.let { checkMessageType(chatMessage = it) })
            //When app is in forground then set title to default title key of Agora (em_push_title)
            pushObject.put("em_push_title",message?.from)
            //When app is in forground then set content to default content key of Agora (em_push_content)
            pushObject.put("em_push_content",message?.body)
            //This is custom key added in data object.
            pushObject.put("chatType",message?.chatType.toString())

          /*   pushObject.put("alert",message?.body?: " ALERT EMPTY")
            titleArgs.put("value1")
            pushObject.put("Title", titleArgs)
            contentArgs.put("value1")
            pushObject.put("Content",contentArgs) */
        }catch (e:Exception){
            Log.d("Fknbkjbg",e.message.toString())
        }

      //  message?.setAttribute("em_push_template",pushObject)
        //This em_apns_ext key is used for pushNotification ext field.
        message?.setAttribute("em_apns_ext",pushObject)
        message?.setAttribute("em_force_notification", true);
    }

    fun checkMessageType(chatMessage: ChatMessage) : String{
      return  if(chatMessage.type == ChatMessage.Type.TXT){
           val messageBody = chatMessage.body as TextMessageBody
           messageBody.message


        }
        else if(chatMessage.type == ChatMessage.Type.FILE){
           val messageBody =   chatMessage as TextMessageBody
           messageBody.message

        }
        else if(chatMessage.type == ChatMessage.Type.IMAGE){            val messageBody =  chatMessage.body as ImageMessageBody
           "${chatMessage.from} has sent you a image: ${messageBody.fileName}"
       }
        else if(chatMessage.type == ChatMessage.Type.LOCATION){        val messageBody =      chatMessage.body as LocationMessageBody
           "${chatMessage.from} has sent you a location: ${messageBody.address}"
       }
        else if(chatMessage.type == ChatMessage.Type.VIDEO){           val messageBody =   chatMessage.body as VideoMessageBody
           "${chatMessage.from} has sent you a video: ${messageBody.fileName}"
       }
        else if(chatMessage.type == ChatMessage.Type.VOICE){         val messageBody =     chatMessage.body as VoiceMessageBody
           "${chatMessage.from} has sent you a voice: ${messageBody.fileName}"
       }
        else if(chatMessage.type == ChatMessage.Type.CMD){         val messageBody =     chatMessage.body as CmdMessageBody
           "${chatMessage.from} -> ${messageBody.action()}"
       }
        else if(chatMessage.type == ChatMessage.Type.COMBINE){       val messageBody =       chatMessage.body as CombineMessageBody
           "${chatMessage.from} sent you a combined message: ${messageBody.fileName}"
       }else{
           val messageBody =   chatMessage.body as TextMessageBody
           messageBody.message
       }



    }
}
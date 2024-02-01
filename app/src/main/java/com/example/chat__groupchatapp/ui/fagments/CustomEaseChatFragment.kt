package com.example.chat__groupchatapp.ui.fagments

import android.util.Log
import io.agora.chat.ChatMessage
import io.agora.chat.uikit.chat.EaseChatFragment
import org.json.JSONArray
import org.json.JSONObject

class CustomEaseChatFragment : EaseChatFragment() {


    override fun addMsgAttrsBeforeSend(message: ChatMessage?) {
        super.addMsgAttrsBeforeSend(message)

        Log.d("ifnvivfnvjfkvkf",message?.body.toString() + "  empty")

        val pushObject = JSONObject()
        val titleArgs = JSONArray()
        val contentArgs = JSONArray()

        //If dont want to push notification to message chatType only then paas "null" to to (to is username).
      //  message?.to = "null"


        try{
            //This is custom key added in data object.
            pushObject.put("alert",message?.body)
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
}
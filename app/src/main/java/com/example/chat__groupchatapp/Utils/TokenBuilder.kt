package com.example.chat__groupchatapp.Utils

import android.content.Context
import com.example.chat__groupchatapp.AgoraTokenUtils.ChatTokenBuilder2
import com.example.chat__groupchatapp.AgoraTokenUtils.RtcTokenBuilder
import com.example.chat__groupchatapp.AgoraTokenUtils.RtcTokenBuilder2
import com.example.chat__groupchatapp.R

object TokenBuilder {


    fun getChatAppoken(context: Context): String {
        val appId = context.getString(R.string.APP_ID)
        val appCertificate = context.getString(R.string.APP_CERTIFICATE)
        val chatAppToken = ChatTokenBuilder2().buildAppToken(appId, appCertificate, getExpiryInSeconds(5))

        return chatAppToken ?: ""
    }

    fun getRtcTokenOfUid(context: Context, uid : Int, channelName : String, role : RtcTokenBuilder2.Role): String {
        val appId = context.getString(R.string.APP_ID)
        val appCertificate = context.getString(R.string.APP_CERTIFICATE)
        val rtcUidToken = RtcTokenBuilder2().buildTokenWithUid(appId, appCertificate, channelName, uid,role, getExpiryInSeconds(5),0 )

        return rtcUidToken ?: ""
    }
}
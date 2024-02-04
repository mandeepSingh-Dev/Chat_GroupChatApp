package com.example.chat__groupchatapp.data.remote.model

data class AgoraNotificationItem(
    var alert: String?= null,
    var chatType : String? = null,
    var is_Incoming_Call : String? = null,
    var channel_Name : String?= null,
    var user_Id : String?= null,
    var call_Type : String?= null,
    var voice_or_video : String?= null,
    var caller_Id : String?= null,
    var call_or_chat : String?= null,
    var reject_call : String?= null,

)
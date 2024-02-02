package com.example.chat__groupchatapp.data.remote.model

data class FirebaseNotificationBody(
    val `data`: NotificationData?,
    val notification: Notification?,
    val to: String?
)
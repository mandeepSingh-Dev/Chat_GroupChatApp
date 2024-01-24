package com.example.chat__groupchatapp.data.remote.model.register

data class RegisterUserResponse(
    val action: String?,
    val application: String?,
    val applicationName: String?,
    val duration: Int?,
    val entities: List<Entity?>?,
    val organization: String?,
    val path: String?,
    val timestamp: Long?,
    val uri: String?
)
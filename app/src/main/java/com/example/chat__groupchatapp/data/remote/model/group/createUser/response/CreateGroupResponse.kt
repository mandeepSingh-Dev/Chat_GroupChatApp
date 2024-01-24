package com.example.chat__groupchatapp.data.remote.model.group.createUser.response

data class CreateGroupResponse(
    val action: String,
    val application: String,
    val applicationName: String,
    val `data`: Data,
    val duration: Int,
    val entities: List<Any>,
    val organization: String,
    val timestamp: Long,
    val uri: String
)
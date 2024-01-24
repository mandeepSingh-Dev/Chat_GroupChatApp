package com.example.chat__groupchatapp.data.remote.model.group.createUser.request

data class CreateGroupRequestBody(
    val description: String,
    val groupname: String,
    val maxusers: Int,
    val members: List<String>,
    val owner: String,
    val `public`: Boolean
)
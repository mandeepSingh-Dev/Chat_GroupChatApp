package com.example.chat__groupchatapp.data.remote.model.users

data class UsersResponse(
    val action: String?,
    val count: Int?,
    val cursor: String?,
    val duration: Int?,
    val entities: List<UserEntity?>?,
    val path: String?,
    val timestamp: Long?,
    val uri: String?
)
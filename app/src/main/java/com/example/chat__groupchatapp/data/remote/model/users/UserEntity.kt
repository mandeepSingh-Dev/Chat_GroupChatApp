package com.example.chat__groupchatapp.data.remote.model.users

data class UserEntity(
    val activated: Boolean?,
    val created: Long?,
    val modified: Long?,
    val nickname: String?,
    val type: String?,
    val username: String?,
    val uuid: String?
)
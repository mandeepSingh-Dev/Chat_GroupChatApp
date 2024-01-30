package com.example.chat__groupchatapp.data.remote.model.user.request.register

data class RegisterUserRequestBody(
    val password: String?,
    val username: String?,
    val nickname: String?
)
package com.example.chat__groupchatapp.data.remote.model.user.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserEntity(
    val activated: Boolean?,
    val created: Long?,
    val modified: Long?,
    val nickname: String?,
    val type: String?,
    val username: String?,
    val uuid: String?
): Parcelable
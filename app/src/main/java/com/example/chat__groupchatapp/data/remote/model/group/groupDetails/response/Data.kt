package com.example.chat__groupchatapp.data.remote.model.group.groupDetails.response

data class Data(
    val affiliations: List<Affiliation>,
    val affiliations_count: Int,
    val allowinvites: Boolean,
    val created: Long,
    val custom: String,
    val description: String,
    val disabled: Boolean,
    val id: String,
    val maxusers: Int,
    val membersonly: Boolean,
    val mute: Boolean,
    val name: String,
    val owner: String,
    val `public`: Boolean
)
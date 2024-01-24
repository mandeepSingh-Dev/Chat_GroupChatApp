package com.example.chat__groupchatapp.data.remote.model.group.groupDetails.response

data class GroupDetailsResponse(
    val action: String,
    val application: String,
    val applicationName: String,
    val count: Int,
    val `data`: List<Data>,
    val description: String,
    val duration: Int,
    val entities: List<Any>,
    val groupname: String,
    val maxusers: Int,
    val members: List<String>,
    val organization: String,
    val owner: String,
    val properties: Properties,
    val `public`: Boolean,
    val timestamp: Long,
    val uri: String
)
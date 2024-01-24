package com.example.chat__groupchatapp.Utils

import io.agora.GroupChangeListener
import io.agora.chat.MucSharedFile

class MGroupChangeListener : GroupChangeListener {
    override fun onInvitationReceived(
        groupId: String?,
        groupName: String?,
        inviter: String?,
        reason: String?
    ) {
        showLog("onInvitationReceived")
    }

    override fun onRequestToJoinReceived(
        groupId: String?,
        groupName: String?,
        applicant: String?,
        reason: String?
    ) {
        showLog("onRequestToJoinReceived")
    }

    override fun onRequestToJoinAccepted(groupId: String?, groupName: String?, accepter: String?) {
                showLog("onRequestToJoinAccepted")

    }

    override fun onRequestToJoinDeclined(
        groupId: String?,
        groupName: String?,
        decliner: String?,
        reason: String?
    ) {
                showLog("onRequestToJoinDeclined")

    }

    override fun onInvitationAccepted(groupId: String?, invitee: String?, reason: String?) {
                showLog("onInvitationAccepted")

    }

    override fun onInvitationDeclined(groupId: String?, invitee: String?, reason: String?) {
                showLog("onInvitationDeclined")

    }

    override fun onUserRemoved(groupId: String?, groupName: String?) {
                showLog("onUserRemoved")

    }

    override fun onGroupDestroyed(groupId: String?, groupName: String?) {
                showLog("onGroupDestroyed")

    }

    override fun onAutoAcceptInvitationFromGroup(
        groupId: String?,
        inviter: String?,
        inviteMessage: String?
    ) {
                showLog("onAutoAcceptInvitationFromGroup")

    }

    override fun onMuteListAdded(groupId: String?, mutes: MutableList<String>?, muteExpire: Long) {
                showLog("onMuteListAdded")

    }

    override fun onMuteListRemoved(groupId: String?, mutes: MutableList<String>?) {
                showLog("onMuteListRemoved")

    }

    override fun onWhiteListAdded(groupId: String?, whitelist: MutableList<String>?) {
                showLog("onWhiteListAdded")

    }

    override fun onWhiteListRemoved(groupId: String?, whitelist: MutableList<String>?) {
                showLog("onWhiteListRemoved")

    }

    override fun onAllMemberMuteStateChanged(groupId: String?, isMuted: Boolean) {
                showLog("onAllMemberMuteStateChanged")

    }

    override fun onAdminAdded(groupId: String?, administrator: String?) {
                showLog("onAdminAdded")

    }

    override fun onAdminRemoved(groupId: String?, administrator: String?) {
                showLog("onAdminRemoved")

    }

    override fun onOwnerChanged(groupId: String?, newOwner: String?, oldOwner: String?) {
                showLog("onOwnerChanged")

    }

    override fun onMemberJoined(groupId: String?, member: String?) {
                showLog("onMemberJoined")

    }

    override fun onMemberExited(groupId: String?, member: String?) {
                showLog("onMemberExited")

    }

    override fun onAnnouncementChanged(groupId: String?, announcement: String?) {
                showLog("onAnnouncementChanged")

    }

    override fun onSharedFileAdded(groupId: String?, sharedFile: MucSharedFile?) {
                showLog("onSharedFileAdded")

    }

    override fun onSharedFileDeleted(groupId: String?, fileId: String?) {
                showLog("onSharedFileDeleted")

    }
}
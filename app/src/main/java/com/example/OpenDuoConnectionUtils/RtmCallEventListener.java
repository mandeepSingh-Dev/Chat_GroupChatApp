package com.example.OpenDuoConnectionUtils;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


public interface RtmCallEventListener {
    void onLocalInvitationReceivedByPeer(LocalInvitation var1);

    void onLocalInvitationAccepted(LocalInvitation var1, String var2);

    void onLocalInvitationRefused(LocalInvitation var1, String var2);

    void onLocalInvitationCanceled(LocalInvitation var1);

    void onLocalInvitationFailure(LocalInvitation var1, int var2);

    void onRemoteInvitationReceived(RemoteInvitation var1);

    void onRemoteInvitationAccepted(RemoteInvitation var1);

    void onRemoteInvitationRefused(RemoteInvitation var1);

    void onRemoteInvitationCanceled(RemoteInvitation var1);

    void onRemoteInvitationFailure(RemoteInvitation var1, int var2);
}

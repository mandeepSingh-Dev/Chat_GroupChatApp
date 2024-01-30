package com.example.OpenDuoConnectionUtils;

public class Global {
    private LocalInvitation mLocalInvitation;
    private RemoteInvitation mRemoteInvitation;

    public LocalInvitation getLocalInvitation() {
        return mLocalInvitation;
    }

    public void setLocalInvitation(LocalInvitation invitation) {
        this.mLocalInvitation = invitation;
    }

    public RemoteInvitation getRemoteInvitation() {
        return mRemoteInvitation;
    }

    public void setRemoteInvitation(RemoteInvitation invitation) {
        this.mRemoteInvitation = invitation;
    }
}

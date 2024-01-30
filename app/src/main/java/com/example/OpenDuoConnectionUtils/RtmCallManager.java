package com.example.OpenDuoConnectionUtils;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import io.agora.rtm.ResultCallback;

public abstract class RtmCallManager {
    public RtmCallManager() {
    }

    public abstract void setEventListener(RtmCallEventListener var1);

    @Nullable
    public abstract LocalInvitation createLocalInvitation(@NonNull String var1);

    public abstract void sendLocalInvitation(@NonNull LocalInvitation var1, ResultCallback<Void> var2);

    public abstract void acceptRemoteInvitation(@NonNull RemoteInvitation var1, ResultCallback<Void> var2);

    public abstract void refuseRemoteInvitation(@NonNull RemoteInvitation var1, ResultCallback<Void> var2);

    public abstract void cancelLocalInvitation(@NonNull LocalInvitation var1, ResultCallback<Void> var2);
}

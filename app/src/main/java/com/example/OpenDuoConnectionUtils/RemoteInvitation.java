package com.example.OpenDuoConnectionUtils;


public interface RemoteInvitation {
    String getCallerId();

    String getContent();

    String getChannelId();

    void setResponse(String var1);

    String getResponse();

    int getState();
}
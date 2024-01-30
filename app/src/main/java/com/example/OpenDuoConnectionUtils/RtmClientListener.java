package com.example.OpenDuoConnectionUtils;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.util.Map;

import io.agora.rtm.RtmMessage;

public interface RtmClientListener {
    void onConnectionStateChanged(int var1, int var2);

    void onMessageReceived(RtmMessage var1, String var2);

    void onTokenExpired();

    void onPeersOnlineStatusChanged(Map<String, Integer> var1);
}

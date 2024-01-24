package com.example.chat__groupchatapp.AgoraTokenUtils;

/**
 * Created by Li on 10/1/2016.
 */
public interface Packable {
    ByteBuf marshal(ByteBuf out);
}
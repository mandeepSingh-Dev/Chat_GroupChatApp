package com.example.OpenDuoConnectionUtils;


import static com.example.chat__groupchatapp.Utils.ExtensionsKt.getExpiryInSeconds;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.example.chat__groupchatapp.AgoraTokenUtils.RtmTokenBuilder2;
import com.example.chat__groupchatapp.R;


import io.agora.rtc2.Constants;
import io.agora.rtc2.RtcEngine;
import io.agora.rtm.ErrorInfo;
import io.agora.rtm.LockEvent;
import io.agora.rtm.MessageEvent;
import io.agora.rtm.PresenceEvent;
import io.agora.rtm.ResultCallback;
import io.agora.rtm.RtmClient;
import io.agora.rtm.RtmConfig;
import io.agora.rtm.RtmConstants;
import io.agora.rtm.RtmEventListener;
import io.agora.rtm.StorageEvent;
import io.agora.rtm.TopicEvent;

public class OpenDuoApplication extends Application {
    private static final String TAG = OpenDuoApplication.class.getSimpleName();

    private RtcEngine mRtcEngine;
    private RtmClient mRtmClient;
    private RtmCallManager rtmCallManager;
    private EngineEventListener mEventListener;
    private Config mConfig;
    private Global mGlobal;

    @Override
    public void onCreate() {
        super.onCreate();
        init();



    }

    private void init() {
        initConfig();
        initEngine();

    }

    private void initConfig() {
        mConfig = new Config(getApplicationContext());
        mGlobal = new Global();
    }

    private void initEngine() {
        String appId = getString(R.string.APP_ID);
        if (TextUtils.isEmpty(appId)) {
            throw new RuntimeException("NEED TO use your App ID, get your own ID at https://dashboard.agora.io/");
        }

        mEventListener = new EngineEventListener();
        try {
            mRtcEngine = RtcEngine.create(getApplicationContext(), appId, mEventListener);
            mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
            mRtcEngine.enableDualStreamMode(true);
            mRtcEngine.enableVideo();
            mRtcEngine.setLogFile(FileUtil.rtmLogFile(getApplicationContext()));


            RtmConfig rtmConfig = new RtmConfig.Builder(appId,mConfig.getUserId()).build();

            mRtmClient = RtmClient.create(rtmConfig);
           // mRtmClient.setLogFile(FileUtil.rtmLogFile(getApplicationContext()));

            if (Config.DEBUG) {
                mRtcEngine.setParameters("{\"rtc.log_filter\":65535}");
                mRtmClient.setParameters("{\"rtm.log_filter\":65535}");
            }

            mRtmClient.addEventListener(new RtmEventListener() {
                @Override
                public void onMessageEvent(MessageEvent event) {
                    RtmEventListener.super.onMessageEvent(event);
                }

                @Override
                public void onPresenceEvent(PresenceEvent event) {
                    RtmEventListener.super.onPresenceEvent(event);
                }

                @Override
                public void onTopicEvent(TopicEvent event) {
                    RtmEventListener.super.onTopicEvent(event);
                }

                @Override
                public void onLockEvent(LockEvent event) {
                    RtmEventListener.super.onLockEvent(event);
                }

                @Override
                public void onStorageEvent(StorageEvent event) {
                    RtmEventListener.super.onStorageEvent(event);
                }

                @Override
                public void onConnectionStateChanged(String channelName, RtmConstants.RtmConnectionState state, RtmConstants.RtmConnectionChangeReason reason) {
                    RtmEventListener.super.onConnectionStateChanged(channelName, state, reason);
                }

                @Override
                public void onTokenPrivilegeWillExpire(String channelName) {
                    RtmEventListener.super.onTokenPrivilegeWillExpire(channelName);
                }
            });
            rtmCallManager.setEventListener(mEventListener);

            String accessToken = new RtmTokenBuilder2().buildToken(appId,getString(R.string.APP_CERTIFICATE),config().getUserId(),getExpiryInSeconds(5) );

            if (TextUtils.equals(accessToken, "") || TextUtils.equals(accessToken, "accessToken"))
            {
                accessToken = null;
            }
            mRtmClient.login( accessToken, new ResultCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {Log.i(TAG, "rtm client login success");}
                @Override
                public void onFailure(ErrorInfo errorInfo) {
                    Log.i(TAG, "rtm client login failed:" + errorInfo.getErrorReason().toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RtcEngine rtcEngine() {
        return mRtcEngine;
    }

    public RtmClient rtmClient() {
        return mRtmClient;
    }

    public void registerEventListener(IEventListener listener) {
        mEventListener.registerEventListener(listener);
    }

    public void removeEventListener(IEventListener listener) {
        mEventListener.removeEventListener(listener);
    }

    public RtmCallManager rtmCallManager() {
        return rtmCallManager;
    }

    public Config config() {
        return mConfig;
    }

    public Global global() {
        return mGlobal;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        destroyEngine();
    }

    private void destroyEngine() {
        RtcEngine.destroy();

        mRtmClient.logout(new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "rtm client logout success");
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                Log.i(TAG, "rtm client logout failed:" + errorInfo.getErrorReason().toString());
            }
        });
    }
}

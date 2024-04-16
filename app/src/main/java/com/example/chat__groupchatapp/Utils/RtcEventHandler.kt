package com.example.agorademoapps.Util

import android.graphics.Rect
import android.util.Log
import io.agora.rtc2.ClientRoleOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.LocalTranscoderConfiguration
import io.agora.rtc2.UserInfo

class RtcEventHandler(
    val mOnUserJoined : (Int) -> Unit,
    val mOnUserOffline : (Int) -> Unit,
    val mOnJoinChannelSuccesss : (String, Int, Int) -> Unit,
    val onErrorr : (Int) -> Unit = {},
    val onLeaveChannell : (RtcStats?) -> Unit = {},
    val onConnectionLostt : () -> Unit = {},
    val onRejoinChannelSuccesss : (toString: String, uid: Int, elapsed: Int) -> Unit = {_,_,_ ->},
    val  onActiveSpeakerr : (uid: Int) -> Unit = {},
    val  onUserMuteAudioo : (uid: Int, muted: Boolean) -> Unit = {_,_ ->},
    val  onUserMuteVideoo :  (uid: Int, muted: Boolean) -> Unit = {_,_ ->},
    val  onUserEnableVideoo :  (uid: Int, muted: Boolean) -> Unit = {_,_ ->},
    val  onUserEnableLocalVideoo :  (uid: Int, muted: Boolean) -> Unit = {_,_ ->},
    val  onVideoSizeChangedd : (source: Constants.VideoSourceType?, uid: Int, width: Int, height: Int, rotation: Int) -> Unit = {_,_,_,_,_ ->},




) : IRtcEngineEventHandler() {
    override fun onError(err: Int) {
        super.onError(err)
        onErrorr(err)
        Log.d("fbkfnbkjgf","onError")
    }

    override fun onCameraReady() {
        super.onCameraReady()

        Log.d("fbkfnbkjgf","onCameraReady")
    }

    override fun onCameraFocusAreaChanged(rect: Rect?) {
        super.onCameraFocusAreaChanged(rect)

        Log.d("fbkfnbkjgf","onCameraFocusAreaChanged")
    }

    override fun onCameraExposureAreaChanged(rect: Rect?) {
        super.onCameraExposureAreaChanged(rect)

        Log.d("fbkfnbkjgf","onCameraExposureAreaChanged")
    }

    override fun onContentInspectResult(result: Int) {
        super.onContentInspectResult(result)

        Log.d("fbkfnbkjgf","onContentInspectResult")
    }

    override fun onSnapshotTaken(
        uid: Int,
        filePath: String?,
        width: Int,
        height: Int,
        errCode: Int,
    ) {
        super.onSnapshotTaken(uid, filePath, width, height, errCode)

        Log.d("fbkfnbkjgf","onSnapshotTaken")
    }

    override fun onFacePositionChanged(
        imageWidth: Int,
        imageHeight: Int,
        faceRectArr: Array<out AgoraFacePositionInfo>?,
    ) {
        super.onFacePositionChanged(imageWidth, imageHeight, faceRectArr)

        Log.d("fbkfnbkjgf","onFacePositionChanged")
    }

    override fun onVideoStopped() {
        super.onVideoStopped()

        Log.d("fbkfnbkjgf","onVideoStopped")
    }

    override fun onLeaveChannel(stats: RtcStats?) {
        super.onLeaveChannel(stats)

        onLeaveChannell(stats)
        Log.d("fbkfnbkjgf","onLeaveChannel")
    }


    override fun onAudioVolumeIndication(speakers: Array<out AudioVolumeInfo>?, totalVolume: Int) {
        super.onAudioVolumeIndication(speakers, totalVolume)

        Log.d("fbkfnbkjgf","onAudioVolumeIndication")
    }

    override fun onLastmileQuality(quality: Int) {
        super.onLastmileQuality(quality)

        Log.d("fbkfnbkjgf","onLastmileQuality")
    }

    override fun onLastmileProbeResult(result: LastmileProbeResult?) {
        super.onLastmileProbeResult(result)

        Log.d("fbkfnbkjgf","onLastmileProbeResult")
    }

    override fun onLocalVideoStat(sentBitrate: Int, sentFrameRate: Int) {
        super.onLocalVideoStat(sentBitrate, sentFrameRate)

        Log.d("fbkfnbkjgf","onLocalVideoStat")
    }






    override fun onFirstLocalVideoFrame(
        source: Constants.VideoSourceType?,
        width: Int,
        height: Int,
        elapsed: Int,
    ) {
        super.onFirstLocalVideoFrame(source, width, height, elapsed)

        Log.d("fbkfnbkjgf","onFirstLocalVideoFrame")
    }

    override fun onConnectionLost() {
        super.onConnectionLost()

        onConnectionLostt()
        Log.d("fbkfnbkjgf","onConnectionLost")
    }

    override fun onConnectionInterrupted() {
        super.onConnectionInterrupted()

        Log.d("fbkfnbkjgf","onConnectionInterrupted")
    }

    override fun onConnectionStateChanged(state: Int, reason: Int) {
        super.onConnectionStateChanged(state, reason)

        Log.d("fbkfnbkjgf","onConnectionStateChanged")
    }

    override fun onNetworkTypeChanged(type: Int) {
        super.onNetworkTypeChanged(type)

        Log.d("fbkfnbkjgf","onNetworkTypeChanged")
    }

    override fun onConnectionBanned() {
        super.onConnectionBanned()

        Log.d("fbkfnbkjgf","onConnectionBanned")
    }

    override fun onMediaEngineLoadSuccess() {
        super.onMediaEngineLoadSuccess()

        Log.d("fbkfnbkjgf","onMediaEngineLoadSuccess")
    }

    override fun onMediaEngineStartCallSuccess() {
        super.onMediaEngineStartCallSuccess()

        Log.d("fbkfnbkjgf","onMediaEngineStartCallSuccess")
    }

    override fun onAudioMixingFinished() {
        super.onAudioMixingFinished()

        Log.d("fbkfnbkjgf","onAudioMixingFinished")
    }

    override fun onRequestToken() {
        super.onRequestToken()

        Log.d("fbkfnbkjgf","onRequestToken")
    }

    override fun onLicenseValidationFailure(error: Int) {
        super.onLicenseValidationFailure(error)

        Log.d("fbkfnbkjgf","onLicenseValidationFailure")
    }

    override fun onAudioRouteChanged(routing: Int) {
        super.onAudioRouteChanged(routing)

        Log.d("fbkfnbkjgf","onAudioRouteChanged")
    }

    override fun onAudioMixingStateChanged(state: Int, reasonCode: Int) {
        super.onAudioMixingStateChanged(state, reasonCode)

        Log.d("fbkfnbkjgf","onAudioMixingStateChanged")
    }

    override fun onAudioMixingPositionChanged(position: Long) {
        super.onAudioMixingPositionChanged(position)

        Log.d("fbkfnbkjgf","onAudioMixingPositionChanged")
    }

    override fun onFirstLocalAudioFramePublished(elapsed: Int) {
        super.onFirstLocalAudioFramePublished(elapsed)

        Log.d("fbkfnbkjgf","onFirstLocalAudioFramePublished")
    }

    override fun onFirstRemoteAudioFrame(uid: Int, elapsed: Int) {
        super.onFirstRemoteAudioFrame(uid, elapsed)

        Log.d("fbkfnbkjgf","onFirstRemoteAudioFrame")
    }

    override fun onFirstRemoteAudioDecoded(uid: Int, elapsed: Int) {
        super.onFirstRemoteAudioDecoded(uid, elapsed)

        Log.d("fbkfnbkjgf","onFirstRemoteAudioDecoded")
    }

    override fun onAudioEffectFinished(soundId: Int) {
        super.onAudioEffectFinished(soundId)

        Log.d("fbkfnbkjgf","onAudioEffectFinished")
    }

    override fun onClientRoleChanged(
        oldRole: Int,
        newRole: Int,
        newRoleOptions: ClientRoleOptions?,
    ) {
        super.onClientRoleChanged(oldRole, newRole, newRoleOptions)

        Log.d("fbkfnbkjgf","onClientRoleChanged")
    }

    override fun onClientRoleChangeFailed(reason: Int, currentRole: Int) {
        super.onClientRoleChangeFailed(reason, currentRole)

        Log.d("fbkfnbkjgf","onClientRoleChangeFailed")
    }

    override fun onRtmpStreamingStateChanged(url: String?, state: Int, errCode: Int) {
        super.onRtmpStreamingStateChanged(url, state, errCode)

        Log.d("fbkfnbkjgf","onRtmpStreamingStateChanged")
    }

    override fun onRtmpStreamingEvent(url: String?, event: Int) {
        super.onRtmpStreamingEvent(url, event)

        Log.d("fbkfnbkjgf","onRtmpStreamingEvent")
    }

    override fun onTranscodingUpdated() {
        super.onTranscodingUpdated()

        Log.d("fbkfnbkjgf","onTranscodingUpdated")
    }

    override fun onTokenPrivilegeWillExpire(token: String?) {
        super.onTokenPrivilegeWillExpire(token)

        Log.d("fbkfnbkjgf","onTokenPrivilegeWillExpire")
    }

    override fun onLocalPublishFallbackToAudioOnly(isFallbackOrRecover: Boolean) {
        super.onLocalPublishFallbackToAudioOnly(isFallbackOrRecover)

        Log.d("fbkfnbkjgf","onLocalPublishFallbackToAudioOnly")
    }

    override fun onChannelMediaRelayStateChanged(state: Int, code: Int) {
        super.onChannelMediaRelayStateChanged(state, code)

        Log.d("fbkfnbkjgf","onChannelMediaRelayStateChanged")
    }

    override fun onChannelMediaRelayEvent(code: Int) {
        super.onChannelMediaRelayEvent(code)

        Log.d("fbkfnbkjgf","onChannelMediaRelayEvent")
    }

    override fun onIntraRequestReceived() {
        super.onIntraRequestReceived()

        Log.d("fbkfnbkjgf","onIntraRequestReceived")
    }

    override fun onUplinkNetworkInfoUpdated(info: UplinkNetworkInfo?) {
        super.onUplinkNetworkInfoUpdated(info)

        Log.d("fbkfnbkjgf","onUplinkNetworkInfoUpdated")
    }



    override fun onEncryptionError(errorType: Int) {
        super.onEncryptionError(errorType)

        Log.d("fbkfnbkjgf","onEncryptionError")
    }

    override fun onPermissionError(permission: Int) {
        super.onPermissionError(permission)

        onPermissionError(permission)
        Log.d("fbkfnbkjgf","onPermissionError")
    }

    override fun onLocalUserRegistered(uid: Int, userAccount: String?) {
        super.onLocalUserRegistered(uid, userAccount)

        Log.d("fbkfnbkjgf","onLocalUserRegistered")
    }

    override fun onUserInfoUpdated(uid: Int, userInfo: UserInfo?) {
        super.onUserInfoUpdated(uid, userInfo)

        Log.d("fbkfnbkjgf","onUserInfoUpdated")
    }

    override fun onUserStateChanged(uid: Int, state: Int) {
        super.onUserStateChanged(uid, state)



        Log.d("fbkfnbkjgf","onUserStateChanged")
    }

    override fun onFirstLocalVideoFramePublished(source: Constants.VideoSourceType?, elapsed: Int) {
        super.onFirstLocalVideoFramePublished(source, elapsed)

        Log.d("fbkfnbkjgf","onFirstLocalVideoFramePublished")
    }

    override fun onAudioSubscribeStateChanged(
        channel: String?,
        uid: Int,
        oldState: Int,
        newState: Int,
        elapseSinceLastState: Int,
    ) {
        super.onAudioSubscribeStateChanged(channel, uid, oldState, newState, elapseSinceLastState)

        Log.d("fbkfnbkjgf","onAudioSubscribeStateChanged")
    }

    override fun onVideoSubscribeStateChanged(
        channel: String?,
        uid: Int,
        oldState: Int,
        newState: Int,
        elapseSinceLastState: Int,
    ) {
        super.onVideoSubscribeStateChanged(channel, uid, oldState, newState, elapseSinceLastState)

        Log.d("fbkfnbkjgf","onVideoSubscribeStateChanged")
    }

    override fun onAudioPublishStateChanged(
        channel: String?,
        oldState: Int,
        newState: Int,
        elapseSinceLastState: Int,
    ) {
        super.onAudioPublishStateChanged(channel, oldState, newState, elapseSinceLastState)

        Log.d("fbkfnbkjgf","onAudioPublishStateChanged")
    }

    override fun onVideoPublishStateChanged(
        source: Constants.VideoSourceType?,
        channel: String?,
        oldState: Int,
        newState: Int,
        elapseSinceLastState: Int,
    ) {
        super.onVideoPublishStateChanged(source, channel, oldState, newState, elapseSinceLastState)

        Log.d("fbkfnbkjgf","onVideoPublishStateChanged")
    }

    override fun onUploadLogResult(requestId: String?, success: Boolean, reason: Int) {
        super.onUploadLogResult(requestId, success, reason)

        Log.d("fbkfnbkjgf","onUploadLogResult")
    }

    override fun onWlAccMessage(reason: Int, action: Int, wlAccMsg: String?) {
        super.onWlAccMessage(reason, action, wlAccMsg)

        Log.d("fbkfnbkjgf","onWlAccMessage")
    }

    override fun onWlAccStats(currentStats: WlAccStats?, averageStats: WlAccStats?) {
        super.onWlAccStats(currentStats, averageStats)

        Log.d("fbkfnbkjgf","onWlAccStats")
    }

    override fun onVideoRenderingTracingResult(
        uid: Int,
        currentEvent: Constants.MEDIA_TRACE_EVENT?,
        tracingInfo: VideoRenderingTracingInfo?,
    ) {
        super.onVideoRenderingTracingResult(uid, currentEvent, tracingInfo)

        Log.d("fbkfnbkjgf","onVideoRenderingTracingResult")
    }

    override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
        super.onJoinChannelSuccess(channel, uid, elapsed)
        mOnJoinChannelSuccesss(channel.toString(), uid, elapsed)

        Log.d("fbkfnbkjgf","onJoinChannelSuccess")
    }

    override fun onRejoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
        super.onRejoinChannelSuccess(channel, uid, elapsed)

        onRejoinChannelSuccesss(channel.toString(), uid, elapsed)
        Log.d("fbkfnbkjgf","onRejoinChannelSuccess")
    }


    override fun onUserJoined(uid: Int, elapsed: Int) {
        super.onUserJoined(uid, elapsed)


        mOnUserJoined(uid)

        Log.d("fbkfnbkjgf","onUserJoined")
    }

    override fun onUserOffline(uid: Int, reason: Int) {
        super.onUserOffline(uid, reason)

        mOnUserOffline(uid)
        Log.d("fbkfnbkjgf","onUserOffline")
    }

    override fun onActiveSpeaker(uid: Int) {
        super.onActiveSpeaker(uid)
        onActiveSpeakerr( uid)

        Log.d("fbkfnbkjgf","onActiveSpeaker")
    }



    override fun onFirstRemoteVideoDecoded(uid: Int, width: Int, height: Int, elapsed: Int) {
        super.onFirstRemoteVideoDecoded(uid, width, height, elapsed)

        Log.d("fbkfnbkjgf","onFirstRemoteVideoDecoded")
    }

    override fun onFirstRemoteVideoFrame(uid: Int, width: Int, height: Int, elapsed: Int) {
        super.onFirstRemoteVideoFrame(uid, width, height, elapsed)

        Log.d("fbkfnbkjgf","onFirstRemoteVideoFrame")
    }

    override fun onUserMuteAudio(uid: Int, muted: Boolean) {
        super.onUserMuteAudio(uid, muted)
        onUserMuteAudioo( uid,muted)

        Log.d("fbkfnbkjgf","onUserMuteAudio")
    }



    override fun onUserMuteVideo(uid: Int, muted: Boolean) {
        super.onUserMuteVideo(uid, muted)
        onUserMuteVideoo( uid,muted)

        Log.d("fbkfnbkjgf","onUserMuteVideo")
    }

    override fun onUserEnableVideo(uid: Int, enabled: Boolean) {
        super.onUserEnableVideo(uid, enabled)
        onUserEnableVideoo( uid,enabled)

        Log.d("fbkfnbkjgf","onUserEnableVideo")
    }

    override fun onUserEnableLocalVideo(uid: Int, enabled: Boolean) {
        super.onUserEnableLocalVideo(uid, enabled)
        onUserEnableLocalVideoo( uid,enabled)

     Log.d("fbkfnbkjgf","onUserEnableLocalVideo")
    }

    override fun onVideoSizeChanged(
        source: Constants.VideoSourceType?,
        uid: Int,
        width: Int,
        height: Int,
        rotation: Int,
    ) {
        super.onVideoSizeChanged(source, uid, width, height, rotation)
        onVideoSizeChangedd( source, uid, width, height, rotation)

        Log.d("fbkfnbkjgf","onVideoSizeChanged")
    }

    private fun onVideoSizeChangedd(source: Constants.VideoSourceType?, uid: Int, width: Int, height: Int, rotation: Int) {

    }

    override fun onRemoteAudioStateChanged(uid: Int, state: Int, reason: Int, elapsed: Int) {
        super.onRemoteAudioStateChanged(uid, state, reason, elapsed)

        Log.d("fbkfnbkjgf","onRemoteAudioStateChanged")
    }

    override fun onRemoteVideoStateChanged(uid: Int, state: Int, reason: Int, elapsed: Int) {
        super.onRemoteVideoStateChanged(uid, state, reason, elapsed)

        Log.d("fbkfnbkjgf","onRemoteVideoStateChanged")
    }

    override fun onRemoteSubscribeFallbackToAudioOnly(uid: Int, isFallbackOrRecover: Boolean) {
        super.onRemoteSubscribeFallbackToAudioOnly(uid, isFallbackOrRecover)

        Log.d("fbkfnbkjgf","onRemoteSubscribeFallbackToAudioOnly")
    }

    override fun onAudioQuality(uid: Int, quality: Int, delay: Short, lost: Short) {
        super.onAudioQuality(uid, quality, delay, lost)

        Log.d("fbkfnbkjgf","onAudioQuality")
    }



    override fun onRemoteVideoStat(
        uid: Int,
        delay: Int,
        receivedBitrate: Int,
        receivedFrameRate: Int,
    ) {
        super.onRemoteVideoStat(uid, delay, receivedBitrate, receivedFrameRate)

        Log.d("fbkfnbkjgf","onRemoteVideoStat")
    }

    override fun onRemoteAudioTransportStats(uid: Int, delay: Int, lost: Int, rxKBitRate: Int) {
        super.onRemoteAudioTransportStats(uid, delay, lost, rxKBitRate)

        Log.d("fbkfnbkjgf","onRemoteAudioTransportStats")
    }

    override fun onRemoteVideoTransportStats(uid: Int, delay: Int, lost: Int, rxKBitRate: Int) {
        super.onRemoteVideoTransportStats(uid, delay, lost, rxKBitRate)

        Log.d("fbkfnbkjgf","onRemoteVideoTransportStats")
    }

    override fun onRhythmPlayerStateChanged(state: Int, errorCode: Int) {
        super.onRhythmPlayerStateChanged(state, errorCode)

        Log.d("fbkfnbkjgf","onRhythmPlayerStateChanged")
    }

    override fun onLocalAudioStateChanged(state: Int, error: Int) {
        super.onLocalAudioStateChanged(state, error)

        Log.d("fbkfnbkjgf","onLocalAudioStateChanged")
    }

    override fun onLocalVideoStateChanged(
        source: Constants.VideoSourceType?,
        state: Int,
        error: Int,
    ) {
        super.onLocalVideoStateChanged(source, state, error)

        Log.d("fbkfnbkjgf","onLocalVideoStateChanged")
    }

    override fun onStreamInjectedStatus(url: String?, uid: Int, status: Int) {
        super.onStreamInjectedStatus(url, uid, status)

        Log.d("fbkfnbkjgf","onStreamInjectedStatus")
    }

    override fun onStreamMessage(uid: Int, streamId: Int, data: ByteArray?) {
        super.onStreamMessage(uid, streamId, data)

        Log.d("fbkfnbkjgf","onStreamMessage")
    }

    override fun onStreamMessageError(
        uid: Int,
        streamId: Int,
        error: Int,
        missed: Int,
        cached: Int,
    ) {
        super.onStreamMessageError(uid, streamId, error, missed, cached)

        Log.d("fbkfnbkjgf","onStreamMessageError")
    }

    override fun onProxyConnected(
        channel: String?,
        uid: Int,
        proxyType: Int,
        localProxyIp: String?,
        elapsed: Int,
    ) {
        super.onProxyConnected(channel, uid, proxyType, localProxyIp, elapsed)

        Log.d("fbkfnbkjgf","onProxyConnected")
    }

    override fun onLocalVideoTranscoderError(
        stream: LocalTranscoderConfiguration.TranscodingVideoStream?,
        error: Int,
    ) {
        super.onLocalVideoTranscoderError(stream, error)

        Log.d("fbkfnbkjgf","onLocalVideoTranscoderError")
    }

}
//package com.example.chat__groupchatapp.backgroundClasses
//
//import android.content.ComponentName
//import android.content.Context
//import android.content.Intent
//import android.net.Uri
//import android.os.Build
//import android.os.Bundle
//import android.telecom.*
//import android.util.Log
//import android.widget.Toast
//import io.agora.chat.uikit.EaseUIKit
//
//class CallHandler(val context : Context) {
//    var telecomManager : TelecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
//    lateinit var phoneAccountHandle: PhoneAccountHandle
//
//    fun init(){
//        val componentName = ComponentName(context,CallConnectionService::class.java)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            phoneAccountHandle = PhoneAccountHandle(componentName, "VoIP Calling")
//            val phoneAccount = PhoneAccount.builder(phoneAccountHandle, "VoIP Calling")
//                .setCapabilities(PhoneAccount.CAPABILITY_CALL_PROVIDER).build()
//            telecomManager.registerPhoneAccount(phoneAccount)
//        }
//    }
//
//
//    fun startIncomingCall(sessionId : String, callType : String){
//        val extras = Bundle()
//        val uri = Uri.fromParts(PhoneAccount.SCHEME_TEL,sessionId,null)
//
//
//
//        extras.putString("SESSIONID",sessionId)
//        extras.putString("RECEIVERTYPE", "receiverType")
//        extras.putString("CALLTYPE", "TYPE")
//        extras.putString("RECEIVERID", "RECEIVERID")
//      //  if (call.receiverType.equals(CometChatConstants.RECEIVER_TYPE_GROUP, ignoreCase = true))
//            extras.putString("NAME", "NAME")
//      //  else
//          //  extras.putString("NAME", "NAME")
//
//      //  if (call.type.equals(CometChatConstants.CALL_TYPE_VIDEO, ignoreCase = true))
//            extras.putInt(TelecomManager.EXTRA_INCOMING_VIDEO_STATE, VideoProfile.STATE_BIDIRECTIONAL)
//     //   else
//         //   extras.putInt(TelecomManager.EXTRA_INCOMING_VIDEO_STATE, VideoProfile.STATE_AUDIO_ONLY)
//
//        extras.putParcelable(TelecomManager.EXTRA_INCOMING_CALL_ADDRESS,uri)
//        extras.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE,phoneAccountHandle)
//        extras.putBoolean(TelecomManager.EXTRA_START_CALL_WITH_SPEAKERPHONE,true)
//
//        var isCallPermitted = false
//
//        isCallPermitted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            telecomManager.isIncomingCallPermitted(phoneAccountHandle)
//        }else{
//            true
//        }
//
//        try{
//            Log.d("startIncomingCall: ",extras.toString()+"\n"+isCallPermitted)
//            telecomManager.addNewIncomingCall(phoneAccountHandle,extras)
//        }catch (e:SecurityException){
//            val intent = Intent()
//            intent.action = TelecomManager.ACTION_CHANGE_PHONE_ACCOUNTS
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            context.startActivity(intent)
//        }catch (e:Exception){
//            Toast.makeText(context,"Error Occured ${e.message}",Toast.LENGTH_LONG).show()
//        }
//    }
//}
//
//
//
//class CallConnectionService(): ConnectionService(){
//
//    override fun onCreateIncomingConnection(
//        connectionManagerPhoneAccount: PhoneAccountHandle?,
//        request: ConnectionRequest?
//    ): Connection {
//        val bundle = request?.extras
//        val sessionId = bundle?.getString("SESSIONID")
//        val name = bundle?.getString("NAME")
//        val receiverType = bundle?.getString("RECEIVERTYPE")
//        val callType = bundle?.getString("CALLTYPE")
//        val receiverID = bundle?.getString("RECEIVERID")
//        val conn = CallConnection(applicationContext, call)
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
//            conn?.connectionProperties = Connection.PROPERTY_SELF_MANAGED
//        }
//        conn?.setCallerDisplayName(name, TelecomManager.PRESENTATION_ALLOWED)
//        conn?.setAddress(request.address, TelecomManager.PRESENTATION_ALLOWED)
//        conn?.setInitializing()
//        conn?.setActive()
//        return conn!!
//    }
//
//    override fun onCreateIncomingConnectionFailed(connectionManagerPhoneAccount: PhoneAccountHandle?, request: ConnectionRequest?) {
//        super.onCreateIncomingConnectionFailed(connectionManagerPhoneAccount, request)
//        Log.e("onCreateIncomingFailed:",request.toString())
//        Toast.makeText(applicationContext,"onCreateIncomingConnectionFailed",Toast.LENGTH_LONG).show();
//    }
//
//}
//
//
//
//
//class CallConnection(context: Context) : Connection(){
//    val TAG = "CallConnection"
//    private var cometchatCall : Call = call
//    private var connectionContext : Context = context
//
//    override fun onCallAudioStateChanged(state: CallAudioState?) {
//        Log.e(TAG, "onCallAudioStateChange:" + state.toString())
//    }
//
//    override fun onDisconnect() {
//        super.onDisconnect()
//        destroyConnection()
//        Log.e(TAG,"onDisconnect")
//        setDisconnected(DisconnectCause(DisconnectCause.LOCAL, "Missed"))
//
//        if (CometChat.getActiveCall()!=null)
//            onDisconnect(CometChat.getActiveCall())
//    }
//
//    fun onDisconnect(call : Call) {
//        Log.e(TAG,"onDisconnect Call: $call")
//        CometChat.rejectCall(call.sessionId,CometChatConstants.CALL_STATUS_CANCELLED,
//            object : CometChat.CallbackListener<Call>() {
//                override fun onSuccess(p0: Call?) {
//                    Log.e(TAG, "onSuccess: reject")
//                }
//
//                override fun onError(p0: CometChatException?) {
//                    Toast.makeText(connectionContext,"Unable to end call due to ${p0?.code}",
//                        Toast.LENGTH_LONG).show()
//                }
//            })
//    }
//
//    override fun onAnswer() {
//        if (cometchatCall.sessionId != null) {
//            CometChat.acceptCall(cometchatCall.sessionId, object : CallbackListener<Call>() {
//                override fun onSuccess(call: Call) {
//                    destroyConnection()
//                    val acceptIntent = Intent(connectionContext, CometChatStartCallActivity::class.java)
//                    acceptIntent.putExtra(UIKitConstants.IntentStrings.SESSION_ID, call.sessionId)
//                    acceptIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                    connectionContext.startActivity(acceptIntent)
//                }
//
//                override fun onError(e: CometChatException) {
//                    destroyConnection()
//                    Toast.makeText(connectionContext, "Call cannot be answered due to " + e.code, Toast.LENGTH_LONG).show()
//                }
//            })
//        }
//    }
//
//    fun destroyConnection() {
//        setDisconnected(DisconnectCause(DisconnectCause.REMOTE, "Rejected"))
//        Log.e(TAG, "destroyConnection" )
//        super.destroy()
//    }
//
//    override fun onReject() {
//        Log.e(TAG, "onReject: " )
//        if (cometchatCall.sessionId != null) {
//            CometChat.rejectCall(cometchatCall.sessionId, CometChatConstants.CALL_STATUS_REJECTED, object : CallbackListener<Call?>() {
//                override fun onSuccess(call: Call?) {
//                    Log.e(TAG, "onSuccess: reject")
//                    destroyConnection()
//                }
//
//                override fun onError(e: CometChatException) {
//                    destroyConnection()
//                    Log.e(TAG, "onErrorReject: " + e.message)
//                    Toast.makeText(connectionContext, "Call cannot be rejected due to" + e.code, Toast.LENGTH_LONG).show()
//                }
//            })
//        }
//    }
//
//
//
//    fun onOutgoingReject() {
//        Log.e(TAG,"onDisconnect")
//        destroyConnection()
//        setDisconnected(DisconnectCause(DisconnectCause.REMOTE, "REJECTED"))
//    }
//
//}
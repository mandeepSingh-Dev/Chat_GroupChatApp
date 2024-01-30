package com.example

import android.content.Intent
import android.telecom.*
import android.util.Log

class MyConnectionService : ConnectionService() {
    override fun onUnbind(intent: Intent?): Boolean {
        Log.d("vmkfmvfvf","onUnbind")
        return super.onUnbind(intent)
    }

    override fun onCreateIncomingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ): Connection {
        Log.d("vmkfmvfvf","onCreateIncomingConnection")
        return super.onCreateIncomingConnection(connectionManagerPhoneAccount, request)
    }

    override fun onCreateIncomingConference(
        connectionManagerPhoneAccount: PhoneAccountHandle,
        request: ConnectionRequest
    ): Conference? {
        Log.d("vmkfmvfvf","onCreateIncomingConference")
        return super.onCreateIncomingConference(connectionManagerPhoneAccount, request)
    }

    override fun onCreateIncomingConnectionFailed(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ) {
        super.onCreateIncomingConnectionFailed(connectionManagerPhoneAccount, request)
        Log.d("vmkfmvfvf","onCreateIncomingConnectionFailed")
    }

    override fun onCreateOutgoingConnectionFailed(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ) {
        super.onCreateOutgoingConnectionFailed(connectionManagerPhoneAccount, request)
        Log.d("vmkfmvfvf","onCreateOutgoingConnectionFailed")
    }

    override fun onCreateIncomingConferenceFailed(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ) {
        Log.d("vmkfmvfvf","onCreateIncomingConferenceFailed")
        super.onCreateIncomingConferenceFailed(connectionManagerPhoneAccount, request)
    }

    override fun onCreateOutgoingConferenceFailed(
        connectionManagerPhoneAccount: PhoneAccountHandle,
        request: ConnectionRequest
    ) {
        super.onCreateOutgoingConferenceFailed(connectionManagerPhoneAccount, request)
        Log.d("vmkfmvfvf","onCreateOutgoingConferenceFailed")
    }

    override fun onCreateOutgoingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ): Connection {
        Log.d("vmkfmvfvf","onCreateOutgoingConnection")
        return super.onCreateOutgoingConnection(connectionManagerPhoneAccount, request)
    }

    override fun onCreateOutgoingConference(
        connectionManagerPhoneAccount: PhoneAccountHandle,
        request: ConnectionRequest
    ): Conference? {
        Log.d("vmkfmvfvf","onCreateOutgoingConference")
        return super.onCreateOutgoingConference(connectionManagerPhoneAccount, request)
    }

    override fun onCreateOutgoingHandoverConnection(
        fromPhoneAccountHandle: PhoneAccountHandle?,
        request: ConnectionRequest?
    ): Connection {
        Log.d("vmkfmvfvf","onCreateOutgoingHandoverConnection")
        return super.onCreateOutgoingHandoverConnection(fromPhoneAccountHandle, request)
    }

    override fun onCreateIncomingHandoverConnection(
        fromPhoneAccountHandle: PhoneAccountHandle?,
        request: ConnectionRequest?
    ): Connection {
        Log.d("vmkfmvfvf","onCreateIncomingHandoverConnection")
        return super.onCreateIncomingHandoverConnection(fromPhoneAccountHandle, request)
    }

    override fun onHandoverFailed(request: ConnectionRequest?, error: Int) {
        super.onHandoverFailed(request, error)
        Log.d("vmkfmvfvf","onUnbind")
    }

    override fun onConference(connection1: Connection?, connection2: Connection?) {
        super.onConference(connection1, connection2)
        Log.d("vmkfmvfvf","onConference")
    }

    override fun onRemoteConferenceAdded(conference: RemoteConference?) {
        super.onRemoteConferenceAdded(conference)
        Log.d("vmkfmvfvf","onRemoteConferenceAdded")
    }

    override fun onRemoteExistingConnectionAdded(connection: RemoteConnection?) {
        super.onRemoteExistingConnectionAdded(connection)
        Log.d("vmkfmvfvf","onRemoteExistingConnectionAdded")
    }

    override fun onConnectionServiceFocusLost() {
        super.onConnectionServiceFocusLost()
        Log.d("vmkfmvfvf","onConnectionServiceFocusLost")
    }

    override fun onConnectionServiceFocusGained() {
        super.onConnectionServiceFocusGained()
        Log.d("vmkfmvfvf","onConnectionServiceFocusGained")
    }
}
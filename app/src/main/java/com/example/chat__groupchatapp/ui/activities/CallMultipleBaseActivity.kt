package com.example.chat__groupchatapp.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.example.chat__groupchatapp.R
import io.agora.chat.callkit.ui.EaseCallMultipleBaseActivity
import io.agora.chat.callkit.widget.EaseCallMemberView
import io.agora.rtc2.video.AgoraVideoFrame

class CallMultipleBaseActivity : EaseCallMultipleBaseActivity() {
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return super.onKeyDown(keyCode, event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun doShowFloatWindow() {
        super.doShowFloatWindow()
    }

    override fun onClick(view: View?) {
        super.onClick(view)
    }

    override fun initView() {
        super.initView()
    }

    override fun createCallMemberView(): EaseCallMemberView {
        return super.createCallMemberView()
    }

    override fun addLiveDataObserver() {
        super.addLiveDataObserver()
    }

    override fun releaseHandler() {
        super.releaseHandler()
    }

    override fun exitChannelDisplay() {
        super.exitChannelDisplay()
    }

}
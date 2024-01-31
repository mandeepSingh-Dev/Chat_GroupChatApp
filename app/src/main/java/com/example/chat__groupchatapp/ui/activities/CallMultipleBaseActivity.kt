package com.example.chat__groupchatapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chat__groupchatapp.R
import io.agora.chat.callkit.ui.EaseCallMultipleBaseActivity

class CallMultipleBaseActivity : EaseCallMultipleBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call_multiple_base)
    }
}
package com.example.chat__groupchatapp.Utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun Context.showToast(message : String?) {
    CoroutineScope(Dispatchers.Main).launch {
        Toast.makeText(this@showToast, message, Toast.LENGTH_SHORT).show()
    }
}
fun Any.showLog(message : String? = "") {
Log.d("LSTATEMENT_LOG",message.toString())
}

package com.example.chat__groupchatapp.Utils

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
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

fun Any.getExpiryInSeconds(days : Int): Int {
    val seconds = 60 //1 minute
    val hours = 60 // 1 hour
    val day = 24 //1 day.

    val oneDaySeconds = seconds*hours*day

    Log.d("fvvlkf",(oneDaySeconds*days).toString())

    return oneDaySeconds*days
}

fun String.bearerToken() = "Bearer $this"

fun View.showSnackbar(message: String = ""){
    val snackbar = Snackbar.make(this,message, Snackbar.LENGTH_SHORT)
    snackbar.animationMode = Snackbar.ANIMATION_MODE_SLIDE
    snackbar.show()
}

fun getExpiryInSeconds(days : Int): Int {
    val seconds = 60 //1 minute
    val hours = 60 // 1 hour
    val day = 24 //1 day.

    val oneDaySeconds = seconds*hours*day

    Log.d("fvvlkf",(oneDaySeconds*days).toString())

    return oneDaySeconds*days
}

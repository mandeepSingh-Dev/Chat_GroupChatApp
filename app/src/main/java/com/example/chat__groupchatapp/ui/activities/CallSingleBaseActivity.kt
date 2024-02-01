package com.example.chat__groupchatapp.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import com.example.chat__groupchatapp.R
import io.agora.chat.callkit.ui.EaseCallSingleBaseActivity
import io.agora.chat.callkit.widget.EaseCallChronometer

class CallSingleBaseActivity : EaseCallSingleBaseActivity() {
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        Log.d("fvlfkmfvf",event.toString())
        return super.onKeyDown(keyCode, event)


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent?.extras?.keySet()?.forEach {
            Log.d("Fvkbnkfvf",it.toString() + " ->  " +intent.extras?.get(it).toString())
        }
        Log.d("gblmgkngb",intent.data.toString())
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        Log.d("Fbkmbkgbm",intent.toString())
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Log.d("gblmgkngb","onBackPressed")
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

        Log.d("vmkmfkff",resultCode.toString())
        Log.d("vmkmfkff",resultCode.toString())
       data?.let {
           it?.extras?.keySet()?.forEach {
               Log.d("bgmbmkgmnbmgkb",it.toString())
           }
       }

    }

    override fun isFloatWindowShowing(): Boolean {
        return super.isFloatWindowShowing()
        Log.d("gblmgkngb","onBackPressed")
    }

    override fun showFloatWindow() {
        super.showFloatWindow()
        Log.d("gblmgkngb","onBackPressed")
    }

    override fun doShowFloatWindow() {
        super.doShowFloatWindow()
        Log.d("gblmgkngb","onBackPressed")
    }

    override fun onClick(view: View?) {
        super.onClick(view)
        Log.d("gblmgkngb","view")
        Log.d("gblmgkngb",view?.id.toString())
        Log.d("fvkmkvf",view?.tag.toString())
        try {
            Log.d("fbmfkvmf", (view as Button).text.toString())
        }catch (e:Exception){}
    }

    override fun initView() {
        super.initView()
        Log.d("gblmgkngb","onBackPressed")
    }

    override fun makeCallStatus() {
        super.makeCallStatus()
        Log.d("gblmgkngb","onBackPressed")
    }



    override fun addLiveDataObserver() {
        super.addLiveDataObserver()
    }

    override fun getChronometerSeconds(cmt: EaseCallChronometer?): Long {
        return super.getChronometerSeconds(cmt)
    }
    override fun releaseHandler() {
        super.releaseHandler()
    }

    override fun exitChannelDisplay() {
        super.exitChannelDisplay()
    }


}
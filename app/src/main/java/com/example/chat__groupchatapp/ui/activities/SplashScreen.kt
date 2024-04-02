package com.example.chat__groupchatapp.ui.activities

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract.Document
import android.text.Html
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.example.chat__groupchatapp.AgoraChatHelper
import com.example.chat__groupchatapp.R
import com.example.chat__groupchatapp.Utils.getExpiryInSeconds
import com.example.chat__groupchatapp.data.remote.AgoraService
import com.example.chat__groupchatapp.databinding.ActivitySplashScreenBinding
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.xml.parsers.DocumentBuilder


class SplashScreen : AppCompatActivity() {

    var agoraChatHelper : AgoraChatHelper? = null

    lateinit var binding : ActivitySplashScreenBinding

    val agoraLibraryFiles = listOf<String>(
        "libagora-fdkaac.so",
        " libagora-ffmpeg.so,",
        " libagora-rtc-sdk.so,",
        " libagora-soundtouch.so,",
        " libagora_ai_echo_cancellation_extension.so",
        " libagora_ai_noise_suppression_extension.so",
        " libagora_audio_beauty_extension.so",
        " libagora_clear_vision_extension.so",
        " libagora_content_inspect_extension.so",
        " libagora_face_capture_extension.so",
        " libagora_face_detection_extension.so",
        " libagora_screen_capture_extension.so",
        " libagora_segmentation_extension.so",
        " libagora_spatial_audio_extension.so",
        " libagora_video_av1_decoder_extension.so",
        " libagora_video_decoder_extension.so",
        " libagora_video_encoder_extension.so",
        " libagora_video_quality_analyzer_extension.so",
        " libaosl.so",
        " libvideo_dec.so",
        " libvideo_enc.so"
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()
        agoraChatHelper = AgoraChatHelper()
        agoraChatHelper?.setUpChatClient(this)


        GlobalScope.launch {
            agoraLibraryFiles?.forEach { fileName ->

                val link = "http://52.66.165.178/agora/arm64-v8a/$fileName"

                async { AgoraSDKDownloader.downloadFile(fileName, this@SplashScreen, "Agora SDK FILE", link) } }
        }


        val okHttpClient = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()

        val gson = GsonBuilder()
            .setLenient()
            .create()


        val agoraService = Retrofit.Builder().baseUrl("http://52.66.165.178/agora/").client(okHttpClient).addConverterFactory(GsonConverterFactory.create(gson)).build().create<AgoraService>()

       val urlsList =  agoraService.getListFiles("arm64-v8a/")


        urlsList.enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("flvkflvmf",response?.body().toString())

                val downloadableByteStream = response.body()?.byteStream()




                /*    var readByte = downloadableByteStream?.read()

                           while(readByte != -1){
                               Log.d("flmkfmvf",readByte.toString())

                               readByte = downloadableByteStream?.read()

                           } */


            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("flvkflvmf",t.message.toString() + " error")

            }
        })


        CoroutineScope(Dispatchers.Main).launch {
        delay(2000)
            if(agoraChatHelper?.isUserLoggedIn() == true){
                startActivity(Intent(this@SplashScreen,UsersGroupActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this@SplashScreen,LoginActivity::class.java))
                finish()
            }
        }


        getExpiryInSeconds(5)
    }






    fun createNotificationChannel(){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(getString(R.string.default_notification_channel_id),getString(R.string.default_notification_channel_id),
                NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }



}

object AgoraSDKDownloader{



    @SuppressLint("Range")
     suspend fun downloadFile(fileName : String,context: Context, desc :String, url : String) = withContext(Dispatchers.IO){

        Log.d("fvblmfkvf",Thread.currentThread().name.toString())
        Log.d("fvb34349039rif",fileName.toString())


        // fileName -> fileName with extension
        val request = DownloadManager.Request(Uri.parse(url))
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setTitle(fileName)
            .setDescription(desc)

            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(false)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileName)


        val downloadManager= context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadID = downloadManager.enqueue(request)

        val query = DownloadManager.Query()
        query.setFilterById(downloadID)



        var job : Job? = null

        job = CoroutineScope(Dispatchers.IO).launch {
            delay(1000)

            var cursor  = downloadManager.query(query)

            while(cursor != null && cursor.moveToFirst()){
                delay(1000)
                if (cursor != null && cursor.moveToFirst()) {

                    val downloadedBytes = cursor?.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)) ?: 0
                    val totalBytes = cursor?.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)) ?: 0

                    Log.d("flbmfkmvf", totalBytes.toString() + " TOTAL BYTES")
                    Log.d("flbmfkmvf", downloadedBytes.toString() + " DOWNLOADED BYTES")

                    if(downloadedBytes >= totalBytes){
                        cursor?.close()
                        job?.cancel()
                    }

                }else{
                    cursor?.close()
                    job?.cancel()
                }

                cursor  = downloadManager.query(query)

            }

        }

    }


}
package com.example.chat__groupchatapp.ui.activities

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.MediaColumns
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.chat__groupchatapp.AgoraChatHelper
import com.example.chat__groupchatapp.BuildConfig
import com.example.chat__groupchatapp.R
import com.example.chat__groupchatapp.Utils.AgoraSDKDownloaderFromRetrofit
import com.example.chat__groupchatapp.Utils.getExpiryInSeconds
import com.example.chat__groupchatapp.data.remote.AgoraService
import com.example.chat__groupchatapp.databinding.ActivitySplashScreenBinding
import com.google.gson.GsonBuilder
import io.agora.rtc2.Constants
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import okio.Path.Companion.toPath
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class SplashScreen : AppCompatActivity() {

    var agoraChatHelper : AgoraChatHelper? = null

    lateinit var binding : ActivitySplashScreenBinding

    val agoraLibraryFiles = listOf<String>(
        "libagora-core.so",
        "libagora-fdkaac.so",
        "libagora-ffmpeg.so",
        "libagora-rtc-sdk.so",
        "libagora-soundtouch.so",
        "libagora_ai_echo_cancellation_extension.so",
        "libagora_ai_noise_suppression_extension.so",
        "libagora_audio_beauty_extension.so",
        "libagora_clear_vision_extension.so",
        "libagora_content_inspect_extension.so",
        "libagora_face_capture_extension.so",
        "libagora_face_detection_extension.so",
        "libagora_screen_capture_extension.so",
        "libagora_segmentation_extension.so",
        "libagora_spatial_audio_extension.so",
        "libagora_udrm3_extension.so",
        "libagora_video_av1_decoder_extension.so",
        "libagora_video_decoder_extension.so",
        "libagora_video_encoder_extension.so",
        "libagora_video_quality_analyzer_extension.so",
        "libaosl.so",
        "libvideo_dec.so",
        "libvideo_enc.so"
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()
        agoraChatHelper = AgoraChatHelper()
        agoraChatHelper?.setUpChatClient(this)


        Build.SUPPORTED_ABIS.forEach {
            Log.d("Vvlfmkf",it.toString())
        }

        Log.d("kbmkfvmnf","${Environment.DIRECTORY_DOWNLOADS}/AgoraFiles")
        Log.d("kbmkfvmnf","${MediaStore.Downloads.DOWNLOAD_URI.toPath()}/AgoraFiles")



    /*     GlobalScope.launch {
            agoraLibraryFiles.forEachIndexed { index, fileName ->

                val link = "http://52.66.165.178/agora/arm64-v8a/$fileName"

               async { AgoraSDKDownloaderFromRetrofit.downloadFiles(fileName, this@SplashScreen, link)}.join()
                Log.d("vkmfkvmfvf", index.toString())
            }

            Log.d("vkmfkvmfvf","Called")
          //  startActivity(Intent(this@SplashScreen,VideoCallActivity::class.java))

        } */


   /*      val okHttpClient = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
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




                 *//*    var readByte = downloadableByteStream?.read()

                           while(readByte != -1){
                               Log.d("flmkfmvf",readByte.toString())

                               readByte = downloadableByteStream?.read()

                           } *//*


            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("flvkflvmf",t.message.toString() + " error")

            }
        })

 */

        Log.d("fvlvmfkvmf","$cacheDir/agora")

//        CoroutineScope(Dispatchers.Main).launch {
       /*  delay(2000)
            if(agoraChatHelper?.isUserLoggedIn() == true){
                startActivity(Intent(this@SplashScreen,UsersGroupActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this@SplashScreen,LoginActivity::class.java))
                finish()
            } */
            startActivity(Intent(this@SplashScreen,VideoCallActivity::class.java))




    /*         val rtcConfig =  RtcEngineConfig()
            rtcConfig.mContext = this@SplashScreen
            rtcConfig.mAppId = getString(R.string.APP_ID)


            java.io.File(cacheDir,AgoraSDKDownloaderFromRetrofit.folderName).listFiles().forEach {
                Log.d("flbmkfmvf",it.name.toString() + " ENABLED")
            }


            rtcConfig.mNativeLibPath = "//"


            var rtcEngine = RtcEngine.create(rtcConfig)

        Log.d("flfklvmf",rtcEngine.toString())

            rtcEngine?.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION)
            rtcEngine?.enableVideo()

        Log.d("flvmkfmvf",rtcEngine?.isSpeakerphoneEnabled.toString())
        Log.d("flvmkfmvf",rtcEngine?.isSpeakerphoneEnabled.toString())

 */


      //  }

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
     suspend fun downloadFile(fileName : String,context: Context, desc :String, url : String) = withContext(Dispatchers.IO) {

        Log.d("fvblmfkvf",Thread.currentThread().name.toString())
        Log.d("fvb34349039rif",fileName.toString())

        val folderName = "AgoraSDKFiles"

       val agoraSdkFolder =  File(context.cacheDir.path,folderName)

        if(!agoraSdkFolder.exists()) {
            agoraSdkFolder.mkdir()
        }

        val destinationUri = Uri.parse(agoraSdkFolder.path)

       val uriT =  FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", agoraSdkFolder)
        Log.d("fvlmfkmvf",uriT.toString())
        Log.d("fvlmfkmvf",uriT.path.toString())

        val uri = Uri.fromFile(agoraSdkFolder)

        Log.d("Dvkvmkvf",uri.path.toString())
        Log.d("Dvkvmkvf",uri.toString())

        // fileName -> fileName with extension
        val request = DownloadManager.Request(Uri.parse(url))
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setTitle(fileName)
            .setDescription(desc)

            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(false)
           // .setDestinationUri(uri)
            .setDestinationUri(uriT)

        val downloadManager= context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadID = downloadManager.enqueue(request)

        val query = DownloadManager.Query()
              query.setFilterById(downloadID)


            delay(1000)

        var cursor  = downloadManager.query(query)

            while(cursor != null && cursor.moveToFirst()){
                delay(1000)
                if (cursor != null && cursor.moveToFirst()) {

                    val downloadedBytes = cursor?.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)) ?: 0
                    val totalBytes = cursor?.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)) ?: 0

                    Log.d("flbmfkmvf", totalBytes.toString() + " TOTAL BYTES  $fileName")
                    Log.d("flbmfkmvf", downloadedBytes.toString() + " DOWNLOADED BYTES $fileName")

                    if(downloadedBytes >= totalBytes){
                        cursor.close()
                        this.cancel()
                    }

                }else{
                    cursor.close()
                    this.cancel()
                }

                cursor  = downloadManager.query(query)
            }

        }
}


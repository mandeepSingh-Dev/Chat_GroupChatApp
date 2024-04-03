package com.example.chat__groupchatapp.Utils

import android.content.Context
import android.provider.MediaStore
import android.util.Log
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Url
import java.io.File
import java.io.FileOutputStream

object AgoraSDKDownloaderFromRetrofit{
    val folderName = "AgoraSDKFiles"

    suspend fun downloadFiles(fileName : String, context: Context, url : String) = withContext(
        Dispatchers.IO){


        val folder =  File(context.filesDir.path,folderName)

        if(!folder.exists()) {
            folder.mkdir()
        }

        var outputFile = File(folder.path,fileName)

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val agoraService = Retrofit.Builder().baseUrl("http://52.66.165.178/agora/").addConverterFactory(
            GsonConverterFactory.create(gson)).build().create<AgoraBackupFilesService>()

        try {
            val downloadFileResponse = agoraService.downloadFileWithRetrofit(url)

            if(downloadFileResponse.isSuccessful){

                val inputStream = downloadFileResponse.body()?.byteStream()

                val totalSpace = inputStream?.available()
                Log.d("Fvlfmvkfm",totalSpace.toString() + " $fileName TOTAL")
                val outputStream = FileOutputStream(outputFile)


                val buffer = ByteArray(8192)
                var bytesRead: Int = 0

                var downloadedBytes = 0

                while (inputStream?.read(buffer)?.also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)

                    downloadedBytes += bytesRead
//                    Log.d("Fvlfmvkfm",bytesRead.toString()+ " $fileName DOWNLOADED BYTES")
                    Log.d("Fvlfmvkfm",downloadedBytes.toString()+ " $fileName DOWNLOADED BYTES")

                }




                outputStream.flush()
                outputStream.close()
                inputStream?.close()


            }else{
                Log.d("flvmkfmvf","Response Failure in Success download files ${downloadFileResponse.code().toString()}")

            }
        }catch (e:Exception){
            Log.d("flvmkfmvf","Failure download files ${e.message}")
        }




    }
}

interface AgoraBackupFilesService {
    @GET
    suspend fun downloadFileWithRetrofit(@Url url: String): Response<ResponseBody>


    @GET
    fun getListFiles(@Url url: String): Call<ResponseBody>
}
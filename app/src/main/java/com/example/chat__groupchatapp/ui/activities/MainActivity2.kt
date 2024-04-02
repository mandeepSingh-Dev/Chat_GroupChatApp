package com.example.chat__groupchatapp.ui.activities

import android.os.Bundle
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AppCompatActivity
import com.example.chat__groupchatapp.R
import com.example.chat__groupchatapp.data.remote.AWSMultipartService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import okio.BufferedSink
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        GlobalScope.launch {
//            uploadFile()
            uploadFil2()
        }

    }


   fun uploadFile(){

       CoroutineScope(Dispatchers.Default).launch {
           val presignedUrl  = "https://buzz-be.s3-eu-west-1.amazonaws.com/chat/b76e557c-e67a-4778-9ac4-b9853f03f647.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20240402T113508Z&X-Amz-SignedHeaders=host&X-Amz-Expires=1799&X-Amz-Credential=AKIAQ6BJZ3SORCYJG4PS%2F20240402%2Feu-west-1%2Fs3%2Faws4_request&X-Amz-Signature=93f07c4ff5af42fb390d2a67d5d55c86ea2cc7a651c416b0abcec01652e2887a"

           val file=  File("/data/data/com.example.chat__groupchatapp/cache/Sample-Video-File-For-Testing.mp4")
           var fileName = "6ac4ee80-9abc-4245-8345-a2720c74e98d.mp4"


           Log.d("flvmkfvf",file.freeSpace.toString())

           var customRequestBody: RequestBody = object : RequestBody() {
               override fun contentType(): MediaType? {
                   return "multipart/form-data".toMediaTypeOrNull()
               }

               @Throws(IOException::class)
               override fun writeTo(sink: BufferedSink) {
                   val buffer = ByteArray(8192)
                   FileInputStream(file).use { `in` ->
                       var bytesRead: Int
                       while (`in`.read(buffer).also { bytesRead = it } != -1) {
                           sink.write(buffer, 0, bytesRead)
                       }
                   }
               }
           }





           val mimeType = MimeTypeMap.getFileExtensionFromUrl(file.path.toString())
           Log.d("flvbvmnfkvmf",mimeType.toString())

           val requestBody = file.readBytes().toRequestBody(mimeType.toMediaTypeOrNull())

           file.readBytes().toRequestBody()

           val multipartBody = MultipartBody.Part.createFormData(
               name = "file", filename = fileName, body = customRequestBody/* requestBody */
           )


           val okHttpClient = OkHttpClient.Builder().build()


           val request = Request.Builder().url(presignedUrl).header("Content-Type", "mp4").put(multipartBody.body).build()

           val retrofit = Retrofit.Builder().baseUrl("https://buzz-be.s3-eu-west-1.amazonaws.com/chat/").addConverterFactory(GsonConverterFactory.create()).build()


           val service = retrofit.create(AWSMultipartService::class.java)

           service.postFilePart(multipartBody).enqueue(object : Callback<ResponseBody>{
               override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                   Log.d("vkfkvmf","RESPONSE")

                   Log.d("fvkmfkvf",response.code().toString())
                   Log.d("fvkmfkvf",response.message().toString())
               }

               override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                   Log.d("vkfkvmf","ERROR -> ${t.message}")
               }
           })

       }





      /*  okHttpClient.newCall(request).enqueue(object : okhttp3.Callback {
           override fun onFailure(call: okhttp3.Call, e: IOException) {
               Log.d("fjknvjfnvf", "FAILURE S3 ${e.message}")
           }



           override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
               if (response.isSuccessful) {
                   Log.d("fjknvjfnvf", "SUCCES Response: ${response.body}")

               } else {
                   Log.d("fjknvjfnvf",   "SUCCESS Error: ${response.message}")
               }           }


       }) */

/*

       val retrofit = Retrofit.Builder().baseUrl("https://buzz-be.s3-eu-west-1.amazonaws.com/chat/").addConverterFactory(GsonConverterFactory.create()).build()


       val service = retrofit.create(AWSMultipartService::class.java)

       service.postFilePart(url = presignedUrl,multipartBody).enqueue(object : Callback<ResponseBody>{
           override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
               Log.d("vkfkvmf","RESPONSE")

               Log.d("fvkmfkvf",response.code().toString())
               Log.d("fvkmfkvf",response.message().toString())
           }

           override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
               Log.d("vkfkvmf","ERROR -> ${t.message}")
           }
       })

*/



   }


    fun uploadFil2(){
        val url = URL("https://buzz-be.s3-eu-west-1.amazonaws.com/chat/4a804ebe-edcc-4c57-b042-ab7a72ada9c4.mp4?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20240402T132654Z&X-Amz-SignedHeaders=host&X-Amz-Expires=1800&X-Amz-Credential=AKIAQ6BJZ3SORCYJG4PS%2F20240402%2Feu-west-1%2Fs3%2Faws4_request&X-Amz-Signature=e138acd28db5cd7ca1b7180c8e362a5bfd2db92c1a55b1ccd18dae2b64b47f1b")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "PUT"
        connection.doOutput = true

        val file=  File("/data/data/com.example.chat__groupchatapp/cache/4-minute Video 1.mp4")
//        val file=  File("/data/data/com.example.chat__groupchatapp/cache/email.svg")
        var fileName = "4a804ebe-edcc-4c57-b042-ab7a72ada9c4.mp4"

        val fileInputStream = FileInputStream(file)
        val fileInputStream_temp = FileInputStream(file)
        val outputStream = connection.outputStream


    /*     var count = fileInputStream.read()
        var total = count

        var list = arrayListOf<Int>()

       while(count != -1){
           total = fileInputStream.read()
           count = fileInputStream.read()

           list.add(total)

           Log.d("fblmfkvmf",total.toString())
       }

        Log.d("flbmfkvmf",list.size.toString()) */

        val buffer = ByteArray(8192)
        var bytesRead: Int

        while (fileInputStream.read(buffer).also { bytesRead = it } != -1) {
            outputStream.write(buffer, 0, bytesRead)
        }

        outputStream.flush()
        outputStream.close()
        fileInputStream.close()

        val responseCode = connection.responseCode

        Log.d("flvkfvmnmf",responseCode.toString())
// Handle response code accordingly

// Handle response code accordingly
        connection.disconnect()
    }


}






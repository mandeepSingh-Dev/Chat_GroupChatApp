package com.example.chat__groupchatapp.data.remote

import android.content.Context
import android.util.Log
import com.example.chat__groupchatapp.Utils.TokenBuilder
import com.example.chat__groupchatapp.Utils.bearerToken
import com.example.chat__groupchatapp.data.remote.model.FirebaseNotificationBody
import com.example.chat__groupchatapp.data.remote.model.group.createUser.request.CreateGroupRequestBody
import com.example.chat__groupchatapp.data.remote.model.group.createUser.response.CreateGroupResponse
import com.example.chat__groupchatapp.data.remote.model.group.groupDetails.response.GroupDetailsResponse
import com.example.chat__groupchatapp.data.remote.model.user.request.register.RegisterUserRequestBody
import com.example.chat__groupchatapp.data.remote.model.user.request.register.RegisterUserResponse
import com.example.chat__groupchatapp.data.remote.model.user.response.UsersResponse
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

object RetrofitClient {
    var retrofitInstance : Retrofit? = null
    /* host = a61.chat.agora.io
    *  org_name = 611085667
    * app_name =  1266092
    */
    var BASE_URL = "https://a61.chat.agora.io/611085667/1266092/"


    private fun getOkHttpClientInterceptor(context: Context): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(AgoraAuthorizationInterceptor(context = context))

        return okHttpClient.build()
    }

    fun getInstance(context : Context): Retrofit? {
        return if(retrofitInstance != null){
            retrofitInstance
        }else{
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getOkHttpClientInterceptor(context))
                .addConverterFactory(GsonConverterFactory.create()).build()
        }

    }

    fun getAgoraService(context: Context) = getInstance(context)?.create(AgoraService::class.java)

}

class AgoraAuthorizationInterceptor(val context : Context) : Interceptor{
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val newRequestBuilder = chain.request().newBuilder()
        return try {

            val chatAppToken = TokenBuilder.getChatAppoken(context)
            newRequestBuilder.addHeader("Authorization", chatAppToken.bearerToken())
            Log.d("okhttp",chatAppToken.toString())
            chain.proceed(newRequestBuilder.build())
        }catch (e:Exception){
            chain.proceed(newRequestBuilder.build())
        }
    }
}

interface AgoraService
{
    @POST("users")
    suspend fun registerUser(@Body registerUserRequestBodyX: RegisterUserRequestBody) :  Response<RegisterUserResponse>

    @GET("users")
    suspend fun getUsers(
        @Query("limit") limit : String,
        @Query("cursor") sursor : String? = null
    ) : Response<UsersResponse>

    @POST("chatgroups")
    suspend fun createGroup(@Body createGroupRequestBody: CreateGroupRequestBody) : Response<CreateGroupResponse>

    @GET("chatgroups/{group_ids}")
    suspend fun getGroupDetails(@Path("group_ids") groupId : String?) : Response<GroupDetailsResponse>

     @DELETE("chatgroups/{group_id}")
    suspend fun deleteGroup(@Path("group_id") groupId : String?)

    @Headers("Authorization: key=AAAAPaBbv8A:APA91bFzjr8bnd42xeL0shPGLDt2DNYFPQXGfGqHXAnapwS3_RjG4josVqkgM8fJ6wcnpjKbiCzUQeB4pliL-cmwS60DxDMPAfoS89w0Bx3vtmVAMYsG_Y9TfcNXmgDkCT93SjWDJ0Is")
    @POST("https://fcm.googleapis.com/fcm/send")
    suspend fun sendFCMNotification(@Body firebaseNotificationBody: FirebaseNotificationBody)


    @GET
    fun downloadFileWithRetrofit(@Url url: String) : Call<ResponseBody>


    @GET
    fun getListFiles(@Url url: String) : Call<ResponseBody>
}


interface AWSMultipartService{

    @Multipart
    @PUT("https://buzz-be.s3-eu-west-1.amazonaws.com/chat/6ac4ee80-9abc-4245-8345-a2720c74e98d.mp4?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20240402T125721Z&X-Amz-SignedHeaders=host&X-Amz-Expires=1800&X-Amz-Credential=AKIAQ6BJZ3SORCYJG4PS%2F20240402%2Feu-west-1%2Fs3%2Faws4_request&X-Amz-Signature=1e4a74659c51f85348943a55da38405da60f3978a2deb2914748263907985817")
    fun postFilePart( @Part filePart : MultipartBody.Part) : Call<ResponseBody>

}
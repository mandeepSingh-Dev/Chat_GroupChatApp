package com.example.chat__groupchatapp.data.remote

import com.example.chat__groupchatapp.data.remote.model.register.RegisterUserRequestBody
import com.example.chat__groupchatapp.data.remote.model.register.RegisterUserResponse
import com.example.chat__groupchatapp.data.remote.model.users.UsersResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

object RetrofitClient {
    var retrofitInstance : Retrofit? = null
    /* host = a61.chat.agora.io
    *  org_name = 611085667
    * app_name =  1266092
    */
    var BASE_URL = "https://a61.chat.agora.io/611085667/1266092/"


    fun getOkHttpClientInterceptor(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor)

        return okHttpClient.build()
    }

    fun getInstance(): Retrofit? {
        return if(retrofitInstance != null){
            retrofitInstance
        }else{
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getOkHttpClientInterceptor())
                .addConverterFactory(GsonConverterFactory.create()).build()
        }

    }

    fun getAgoraService() = getInstance()?.create(AgoraService::class.java)

}

interface AgoraService
{
    @POST("users")
    suspend fun registerUser(@Body registerUserRequestBodyX: RegisterUserRequestBody) :  Response<RegisterUserResponse>

    @GET("users")
    suspend fun getUsers(
        @Header("Authorization") chatAppToken : String?,
        @Query("limit") limit : String,
        @Query("cursor") sursor : String? = null
    ) : Response<UsersResponse>
}
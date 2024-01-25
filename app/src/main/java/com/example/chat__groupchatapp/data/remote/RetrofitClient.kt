package com.example.chat__groupchatapp.data.remote

import android.content.Context
import android.util.Log
import com.example.chat__groupchatapp.AgoraTokenUtils.ChatTokenBuilder2
import com.example.chat__groupchatapp.R
import com.example.chat__groupchatapp.Utils.TokenBuilder
import com.example.chat__groupchatapp.Utils.bearerToken
import com.example.chat__groupchatapp.Utils.getExpiryInSeconds
import com.example.chat__groupchatapp.data.remote.model.group.createUser.request.CreateGroupRequestBody
import com.example.chat__groupchatapp.data.remote.model.group.createUser.response.CreateGroupResponse
import com.example.chat__groupchatapp.data.remote.model.group.groupDetails.response.GroupDetailsResponse
import com.example.chat__groupchatapp.data.remote.model.user.request.register.RegisterUserRequestBody
import com.example.chat__groupchatapp.data.remote.model.user.request.register.RegisterUserResponse
import com.example.chat__groupchatapp.data.remote.model.user.response.UsersResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import kotlin.coroutines.coroutineContext

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


}
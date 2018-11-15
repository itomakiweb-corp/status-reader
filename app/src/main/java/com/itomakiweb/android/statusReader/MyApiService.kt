package com.itomakiweb.android.statusReader

import com.itomakiweb.android.statusReader.service.Stery
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface MyApiService {
    @Headers("Content-Type: application/json")
    @POST("userfeelings")
    fun createUser(@Body user: Stery): Call<ResponseBody>

    @GET("userfeelings")
    fun getUser(): Call<String>
}

data class MyResponse(var status : String)
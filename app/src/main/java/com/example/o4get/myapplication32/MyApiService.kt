package com.example.o4get.myapplication32

import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface MyApiService {
    @Headers("Content-Type: application/json")
    @POST("userfeelings")
    fun createUser(@Body user: UserFeeling): Call<ResponseBody>

    @GET("userfeelings")
    fun getUser(): Call<String>
}

data class MyResponse(var status : String)
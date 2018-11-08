package com.itomakiweb.android.statusReader.service

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface StatusReaderApiClientInterface {
    @Headers("Content-Type: application/json")
    @POST("userfeelings")
    fun createUserFeeling(@Body user: UserFeeling): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("userdata")
    fun createUserData(@Body userdata: UserData): Call<UserData>
}
package com.itomakiweb.android.statusReader.service

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface StatusReaderApiClientInterface {
    @Headers("Content-Type: application/json")
    @POST("stery")
    fun createStery(@Body stery: Stery): Call<Score>

    @Headers("Content-Type: application/json")
    @POST("userdata")
    fun createUserData(@Body userdata: UserData): Call<UserData>

    @Headers("Accept: application/json")
    @GET("seed/all")
    fun getAllSeeds(): Call<List<Seed>>

    @Headers("Accept: application/json")
    @GET("seed")
    fun getMySeeds(@Query("userId") userId: String): Call<List<Seed>>

    @Headers("Content-Type: application/json")
    @POST("seed")
    fun createSeed(@Body seed: Seed): Call<Seed>

}
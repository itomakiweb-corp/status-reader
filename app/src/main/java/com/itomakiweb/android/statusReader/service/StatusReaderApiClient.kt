package com.itomakiweb.android.statusReader.service

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object StatusReaderApiClient {
    val Instance by lazy { createService() }
    private fun createService(): StatusReaderApiClientInterface {
        val baseUrl = "https://todoapidozeu.azurewebsites.net/api/"
//    val baseUrl = "https://localhost:44322/"
        val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .baseUrl(baseUrl)
                .build()
        return retrofit.create(StatusReaderApiClientInterface::class.java)
    }
}
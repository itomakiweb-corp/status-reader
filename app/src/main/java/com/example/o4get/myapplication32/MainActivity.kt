package com.example.o4get.myapplication32

import android.app.Application
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //var webview : WebView = myWebView
        myWebView.settings.javaScriptEnabled = true
        myWebView.webViewClient = WebViewClient()
        myWebView.loadUrl("https://www.youtube.com/watch?v=6N74Q467uok")


        button.setOnClickListener{
            var udata = UserFeeling("okabe",
                    "come1 desu", "come2 desu", "come3 desu", "2018/10/04 19:00:00"
            )

            try {
                var userFeelingsService: UserFeelingsService = UserFeelingsService(this.application)
                //TODO okabe 次の行でおちる
                var res = userFeelingsService.createService().createUser(udata).execute()
                if (res.isSuccessful){
                    var som1 = 1
                }
                else{
                    var som2 = 2
                }
            }
            catch (e: IOException){

            }
        }
    }

}

interface ApiClient {
    @POST("/userfeelings")
    fun createUser(@Body user: UserFeeling): Call<UserFeeling>
}

data class UserFeeling(var userName: String,
                       var comment1: String,
                       var comment2: String,
                       var comment3: String,
                       var issuedTime: String)

class UserFeelingsService(val application: Application){

    fun createService(): ApiClient {
        val baseUrl = "https://todoapidozeu.azurewebsites.net/api"
//        val baseUrl = "https://localhost:44322"
        val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build()
        return retrofit.create(ApiClient::class.java)
    }

}
package com.itomakiweb.android.statusReader

import  android.os.Handler
import android.app.AlertDialog
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {
    private val userFeelingsService by lazy { createService() }

    private var comment1Text : String = ""
    private var comment2Text : String = ""
    private var comment3Text : String = ""

    val handler = Handler()
    var inputEnableTime = 0
    var timeLimit = 0
    var timeCount = 0
    var isTimeout = false
    var inputStart : Date = Date()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputEnableTime = 5
        //TODO okabe サインインしたユーザの権限からtimeLimitを確定する？
        timeLimit = 36 + inputEnableTime

        comment1.isEnabled = false
        button.isEnabled = false

        comment2.isEnabled = false
        button2.isEnabled = false

        comment3.isEnabled = false
        button3.isEnabled = false



        button.setOnClickListener{
            comment1.isEnabled = false
            button.isEnabled = false
            if (!isTimeout){
                comment2.isEnabled = true
                button2.isEnabled = true
            }
        }
        button2.setOnClickListener{
            comment2.isEnabled = false
            button2.isEnabled = false
            if (!isTimeout){
                comment3.isEnabled = true
                button3.isEnabled = true
            }
        }
        button3.setOnClickListener{
            comment3.isEnabled = false
            button3.isEnabled = false
            sendComment(Date())
        }

        //var webview : WebView = myWebView
        myWebView.settings.javaScriptEnabled = true
        myWebView.settings.loadWithOverviewMode = false
        myWebView.webViewClient = object : WebViewClient(){

            override fun onPageFinished(view: WebView?, url: String?) {
                //INFO okabe onPageStartedを２回通っても、ここは１回（https://m.youtube.com/）
                handler.post(runnable)
            }

//            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
//                //TODO okabe URLによっては複数回通るみたいなのでつかいづらい。（１、https://www.youtube.com/　２、https://m.youtube.com/）
//                handler.post(runnable)
//            }
        }
        myWebView.loadUrl("https://www.youtube.com/watch?v=6N74Q467uok")

    }

    val runnable = object : Runnable {
        override fun run() {
            timeCount++

            if (timeCount == inputEnableTime){
                //TODO okabe これは暫定処理。見始めてから５秒で入力可能
                comment1.isEnabled = true
                button.isEnabled = true
                inputStart = Date()
            }

            if (timeCount <= timeLimit){
                handler.postDelayed(this, 1000)
                textView.text = timeCount.toString()
            }
            else{
                isTimeout = true
                sendComment(Date())
            }
        }
    }

    fun sendComment(sendTime : Date){
        handler.removeCallbacks(runnable)

        comment1.isEnabled = false
        button.isEnabled = false

        comment2.isEnabled = false
        button2.isEnabled = false

        comment3.isEnabled = false
        button3.isEnabled = false

        comment1Text = comment1.text.toString()
        comment2Text = comment2.text.toString()
        comment3Text = comment3.text.toString()

        var elapsedMilliSec = sendTime.time - inputStart.time
        var elapsedDate = Date(elapsedMilliSec)

        var udata = UserFeeling(
                "okabe",
                 comment1Text, comment2Text, comment3Text, elapsedMilliSec, SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(Date())
        )


        try {
            var res = userFeelingsService.createUser(udata).enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                    AlertDialog.Builder(this@MainActivity)
                            .setTitle("Send Data")
                            .setMessage("failed.").show()
                }

                override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            AlertDialog.Builder(this@MainActivity)
                                    .setTitle("Send Data")
                                    .setMessage("success.").show()
                        }
                    }
                }
            })
        }
        catch (e: Throwable){
            AlertDialog.Builder(this@MainActivity)
                    .setTitle("Send Data")
                    .setMessage(e.message).show()
        }
    }

}

fun createService(): MyApiService {
    val baseUrl = "https://todoapidozeu.azurewebsites.net/api/"
//    val baseUrl = "https://localhost:44322/"
    val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .baseUrl(baseUrl)
            .build()
    return retrofit.create(MyApiService::class.java)
}



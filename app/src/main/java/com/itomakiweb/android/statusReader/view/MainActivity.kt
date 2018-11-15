package com.itomakiweb.android.statusReader.view

import  android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.itomakiweb.android.statusReader.R
import com.itomakiweb.android.statusReader.model.UserAuthManager
import com.itomakiweb.android.statusReader.service.Score
import com.itomakiweb.android.statusReader.service.Seed
import com.itomakiweb.android.statusReader.service.StatusReaderApiClient
import com.itomakiweb.android.statusReader.service.Stery
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {
//    private val userFeelingsService by lazy { createService() }

//    private var comment1Text : String = ""
//    private var comment2Text : String = ""
//    private var comment3Text : String = ""

    val handler = Handler()
    var inputEnableTime = 0
    var timeLimit = 0
    var timeCount = 0
    var inputTimeCount = 0
    var isTimeout = false
    var inputStart : Date = Date()
    var seedUrl = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var seed: Seed
        try {
            seed = intent.getSerializableExtra("seed") as Seed
            inputEnableTime = seed.inputStartTime
            //TODO okabe サインインしたユーザの権限からtimeLimitを確定する？
            timeLimit = seed.inputEndTime
            textView.text = ""
            inputTimeCount -= inputEnableTime
            seedUrl = seed.seedUrl
        }
        catch (error: Exception){
            finish()
        }


//        comment1.isEnabled = false
//        button.isEnabled = false
//
//        comment2.isEnabled = false
//        button2.isEnabled = false
//
//        comment3.isEnabled = false
//        button3.isEnabled = false



        button.setOnClickListener{
            comment1.isEnabled = false
            button.isEnabled = false
            comment_con1.visibility = View.GONE
            if (!isTimeout){
                comment2.isEnabled = true
                button2.isEnabled = true
                comment_con2.visibility = View.VISIBLE
                comment2.requestFocusFromTouch()
            }
        }
        button2.setOnClickListener{
            comment2.isEnabled = false
            button2.isEnabled = false
            comment_con2.visibility = View.GONE
            if (!isTimeout){
                comment3.isEnabled = true
                button3.isEnabled = true
                comment_con3.visibility = View.VISIBLE
                comment3.requestFocusFromTouch()
            }
        }
        button3.setOnClickListener{
            comment3.isEnabled = false
            button3.isEnabled = false
            comment_con3.visibility = View.GONE
            sendComment(Date())
        }

        myWebView.settings.javaScriptEnabled = true
        myWebView.settings.loadWithOverviewMode = false
        myWebView.webViewClient = object : WebViewClient(){

            override fun onPageFinished(view: WebView?, url: String?) {
                //INFO okabe onPageStartedを２回通っても、ここは１回（https://m.youtube.com/）
                handler.post(runnable)
            }

//            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
//                //INFO okabe URLによっては複数回通るみたいなのでつかいづらい。（１、https://www.youtube.com/　２、https://m.youtube.com/）
//                handler.post(runnable)
//            }
        }

        try {
            myWebView.loadUrl(seedUrl)
        }
        catch (error: Exception){
            Toast.makeText(this,"can not load url ${seedUrl}", Toast.LENGTH_LONG)
        }

    }

    val runnable = object : Runnable {
        override fun run() {
            timeCount++
            inputTimeCount++

            if (timeCount == inputEnableTime){
                //TODO okabe これは暫定処理。見始めてから５秒で入力可能
                comment1.isEnabled = true
                button.isEnabled = true
                comment_con1.visibility = View.VISIBLE
                comment1.requestFocusFromTouch()
                inputStart = Date()
            }

            if (timeCount <= timeLimit){
                handler.postDelayed(this, 1000)
                if(inputTimeCount >= 0){
                    textView.text = inputTimeCount.toString()
                }
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

        comment_con1.visibility = View.VISIBLE
        comment_con2.visibility = View.VISIBLE
        comment_con3.visibility = View.VISIBLE

        var elapsedMilliSec = sendTime.time - inputStart.time

        var udata = Stery(
                userName= UserAuthManager.CurrentUserName,
                userId =  UserAuthManager.CurrentUserId,
                comment1 =  comment1.text.toString(),
                comment2 = comment2.text.toString(),
                comment3 = comment3.text.toString(),
                elapsedMilliSec = elapsedMilliSec
        )


        StatusReaderApiClient.Instance.createStery(udata).enqueue(object : Callback<Score> {
            override fun onFailure(call: Call<Score>?, t: Throwable?) {

                AlertDialog.Builder(this@MainActivity)
                        .setTitle("Send Data")
                        .setMessage("failed.")
                        .show()
            }

            override fun onResponse(call: Call<Score>?, response: Response<Score>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        AlertDialog.Builder(this@MainActivity).apply {
                            setTitle("Send Data")
                            setMessage("SCORE: ${it.currentScore}\nTOTAL:${it.totalScore}\nRank: ${it.rank}")
                            setPositiveButton("OK"){_,_ ->
                                finish()
                            }
                            show()
                        }

                    }
                }
                else{
                    AlertDialog.Builder(this@MainActivity)
                            .setTitle("Send Data")
                            .setMessage("response is not Successful.")
                            .show()
                }
            }
        })
    }

}


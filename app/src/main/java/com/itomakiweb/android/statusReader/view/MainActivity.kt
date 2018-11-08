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
import com.itomakiweb.android.statusReader.service.StatusReaderApiClient
import com.itomakiweb.android.statusReader.service.UserFeeling
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {
//    private val userFeelingsService by lazy { createService() }

    private var comment1Text : String = ""
    private var comment2Text : String = ""
    private var comment3Text : String = ""

    val handler = Handler()
    var inputEnableTime = 0
    var timeLimit = 0
    var timeCount = 0
    var inputTimeCount = 0
    var isTimeout = false
    var inputStart : Date = Date()

    private var userId = ""
    private var userName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputEnableTime = 5
        //TODO okabe サインインしたユーザの権限からtimeLimitを確定する？
        timeLimit = 36 + inputEnableTime
        textView.text = ""
        inputTimeCount -= inputEnableTime

                comment1.isEnabled = false
        button.isEnabled = false

        comment2.isEnabled = false
        button2.isEnabled = false

        comment3.isEnabled = false
        button3.isEnabled = false



        button.setOnClickListener{
            comment1.isEnabled = false
            button.isEnabled = false
            comment_con1.visibility = View.GONE
            if (!isTimeout){
                comment2.isEnabled = true
                button2.isEnabled = true
                comment_con2.visibility = View.VISIBLE
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
        myWebView.loadUrl("https://www.youtube.com/watch?v=6N74Q467uok")

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

        comment1Text = comment1.text.toString()
        comment2Text = comment2.text.toString()
        comment3Text = comment3.text.toString()

        var elapsedMilliSec = sendTime.time - inputStart.time
        var elapsedDate = Date(elapsedMilliSec)

        var udata = UserFeeling(
                UserAuthManager.CurrentUserName, UserAuthManager.CurrentUserId,
                comment1Text, comment2Text, comment3Text,
                elapsedMilliSec, SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(Date())
        )


        try {
            var res = StatusReaderApiClient.Instance.createUserFeeling(udata).enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {

                    AlertDialog.Builder(this@MainActivity)
                            .setTitle("Send Data")
                            .setMessage("failed.")
                            .show()
                }

                override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            var content = it.string()
                            AlertDialog.Builder(this@MainActivity).apply {
                                setTitle("Send Data")
                                setMessage("success.")
//                                setPositiveButton("",{_, _ ->
//                                    Toast.makeText(this@MainActivity, "Dialog OK", Toast.LENGTH_LONG).show()
//                                })
                                setPositiveButton("OK"){_,_ ->
                                    Toast.makeText(context, "Dialog OK", Toast.LENGTH_LONG).show()
                                }
                                setNegativeButton("RETURN"){_,_->
                                    finish()
                                }
                                show()
                            }

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


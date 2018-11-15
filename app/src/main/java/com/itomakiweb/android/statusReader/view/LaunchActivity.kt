package com.itomakiweb.android.statusReader.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.content.Intent
import android.support.v7.app.AlertDialog
import com.itomakiweb.android.statusReader.R
import com.itomakiweb.android.statusReader.model.UserAuthManager
import java.lang.Exception


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class LaunchActivity : AppCompatActivity() {
    private val handler = Handler()
    private var isTimeEnough = false
    private var shouldSignIn = false
    private var isFinishedConfirm = false
    private var ProcessingFinishLogo = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()
        handler.postDelayed(runnable, 2000)
        try {
            shouldSignIn = UserAuthManager.shouldSignIn(this@LaunchActivity)
            isFinishedConfirm = true
            if(isTimeEnough){
                logoFinish()
            }
        }
        catch (error :Exception){
            AlertDialog.Builder(this@LaunchActivity).apply {
                setTitle("Auth Error")
                setTitle(error.message)
                setNeutralButton("OK"){_,_-> finish()}
            }
        }
    }

    private val runnable = object : Runnable {
        override fun run() {
            //TODO okabe 本来必要な処理が別途あれば、そちらでフラグをtrueにすること
            isTimeEnough = true
            if(isFinishedConfirm){
                logoFinish()
            }
        }
    }

    fun logoFinish(){
        if(ProcessingFinishLogo){
            return
        }
        ProcessingFinishLogo = true
        handler.removeCallbacks(runnable)

        if(shouldSignIn){
            val logoIntent = Intent(this@LaunchActivity, TitleActivity::class.java)
            startActivity(logoIntent)
        }
        else{
            val homeIntent = Intent(this@LaunchActivity, HomeActivity::class.java)
            startActivity(homeIntent)
        }
        finish()
    }

}

package com.itomakiweb.android.statusReader.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.content.Intent
import com.itomakiweb.android.statusReader.R

class Launch2Activity : AppCompatActivity() {
    private val handler = Handler()
    private var shouldFinish = false

    override fun onStart() {
        super.onStart()
        handler.postDelayed(runnable, 2000)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private val runnable = object : Runnable {
        override fun run() {
            //TODO okabe 本来必要な処理が別途あれば、そちらでフラグをtrueにすること
            shouldFinish = true
            if(shouldFinish){
                logoFinish()
            }
        }
    }

    fun logoFinish(){
        handler.removeCallbacks(runnable)
        val logoIntent = Intent(this@Launch2Activity, TitleActivity::class.java)
        startActivity(logoIntent)
        finish()
    }

}

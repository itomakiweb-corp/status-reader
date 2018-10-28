package com.itomakiweb.statusReader

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import kotlinx.android.synthetic.main.activity_logo.*
import android.content.Intent



/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class LogoActivity : AppCompatActivity() {
    val handler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_logo)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        handler.post(runnable)
    }

    val runnable = object : Runnable {
        override fun run() {
            handler.postDelayed(this, 2000)
            logoFinish()
        }
    }

    fun logoFinish(){
        handler.removeCallbacks(runnable)
        val logoIntent = Intent(this@LogoActivity, TitleActivity::class.java)
        startActivity(logoIntent)
        finish()
    }

}

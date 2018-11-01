package com.itomakiweb.android.statusReader

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*

class HomeActivity : AppCompatActivity() {
    var userId = ""
    var userName = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        var extras = intent.extras
        if(extras != null){
            userId = extras.getString("id")
            userName = extras.getString("name")
        }

        button4.setOnClickListener {
            val intent = Intent(this@HomeActivity, MainActivity::class.java)
            intent.putExtra("id",userId)
            intent.putExtra("name",userName)
            startActivity(intent)
        }
    }

}

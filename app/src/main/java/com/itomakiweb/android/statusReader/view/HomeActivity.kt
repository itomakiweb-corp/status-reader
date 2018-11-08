package com.itomakiweb.android.statusReader.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.itomakiweb.android.statusReader.R
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {
    var userId = ""
    var userName = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        button4.setOnClickListener {
            val intent = Intent(this@HomeActivity, MainActivity::class.java)
//            intent.putExtra("id",userId)
//            intent.putExtra("name",userName)
            startActivity(intent)
        }
    }

}

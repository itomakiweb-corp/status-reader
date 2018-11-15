package com.itomakiweb.android.statusReader.view

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.itomakiweb.android.statusReader.R
import com.itomakiweb.android.statusReader.model.UserAuthManager
import com.itomakiweb.android.statusReader.service.Seed
import com.itomakiweb.android.statusReader.service.StatusReaderApiClient
import kotlinx.android.synthetic.main.activity_my_seed.*
import kotlinx.android.synthetic.main.create_seed_layout.*
import kotlinx.android.synthetic.main.create_seed_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class MySeedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_seed)

        floatingActionButton.setOnClickListener {
            it.isEnabled = false

            val inflater = LayoutInflater.from(this@MySeedActivity)
            val view = inflater.inflate(R.layout.create_seed_layout, null)
            AlertDialog.Builder(this@MySeedActivity).apply {
                setTitle("Create Seed")
                setView(view)
                setPositiveButton("OK"){_,_ ->
                    it.isEnabled = false
                    try {
                        var keys = ""
                        keys += if (view.create_seed_layout_key1.text.isNullOrBlank()) "" else view.create_seed_layout_key1.text
                        keys += if (view.create_seed_layout_key2.text.isNullOrBlank()) "" else "," + view.create_seed_layout_key2.text
                        keys += if (view.create_seed_layout_key3.text.isNullOrBlank()) "" else "," + view.create_seed_layout_key3.text
                        val seed = Seed(
                                uploadUserName = UserAuthManager.CurrentUserName,
                                uploadUserId = UserAuthManager.CurrentUserId,
                                seedType = "Youtube",
                                seedTitle = view.create_seed_layout_title.text.toString(),
                                seedUrl = view.create_seed_layout_url.text.toString(),
                                keySteries = keys,
                                inputStartTime = 5,
                                inputEndTime = 41
                        )

                        createSeed(seed)
                    }
                    catch (error: Exception){
                        get_seed_message.text = error.message
                    }
                }
                setNegativeButton("CANCEL"){_,_->
                    it.isEnabled = true
                }

                show()
            }
        }

        try {
            getMySeedList()
        }
        catch (e: Throwable){
            AlertDialog.Builder(this@MySeedActivity)
                    .setTitle("Get Seeds")
                    .setMessage(e.message).show()
        }
        floatingActionButton.isEnabled = true
    }

    private fun createSeed(seed: Seed){
        progressBar_mySeed.visibility = View.VISIBLE
        StatusReaderApiClient.Instance.createSeed(seed).enqueue(object : Callback<Seed> {
            override fun onFailure(call: Call<Seed>?, t: Throwable?) {

                AlertDialog.Builder(this@MySeedActivity)
                        .setTitle("Create Seeds")
                        .setMessage("failed.")
                        .show()
                progressBar_mySeed.visibility = View.GONE
            }

            override fun onResponse(call: Call<Seed>?, response: Response<Seed>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        try {
                            getMySeedList()
                        }
                        catch (error: Exception){
                            val tempText = TextView(this@MySeedActivity).apply {
                                text = "${error?.message}"
                            }
                            mySeedList.addView(tempText)
                        }
                    }
                }
                else{
                    AlertDialog.Builder(this@MySeedActivity)
                            .setTitle("Create Seeds")
                            .setMessage("response is not Successful.")
                            .show()
                }
                progressBar_mySeed.visibility = View.GONE
            }
        })
    }

    private fun getMySeedList(){
        get_seed_message.text = ""
        StatusReaderApiClient.Instance.getMySeeds(UserAuthManager.CurrentUserId).enqueue(object : Callback<List<Seed>> {
            override fun onFailure(call: Call<List<Seed>>?, t: Throwable?) {

                get_seed_message.text = "Getting My Seeds: failed"
                progressBar_mySeed.visibility = View.GONE
            }

            override fun onResponse(call: Call<List<Seed>>?, response: Response<List<Seed>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        try {
                            createSeedList(it)
                        }
                        catch (error: Exception){
                            val tempText = TextView(this@MySeedActivity).apply {
                                text = "${error?.message}"
                            }
                            mySeedList.addView(tempText)
                        }
                    }
                }
                else{
                    get_seed_message.text = "Getting My Seeds: response is not Successful"
                }
                progressBar_mySeed.visibility = View.GONE
            }
        })
    }

    private fun createSeedList(list: List<Seed>){
        mySeedList.removeAllViews()
        if(list.isEmpty()){
            val tempText = TextView(this@MySeedActivity).apply {
                text = "no seeds."
            }
            mySeedList.addView(tempText)
            return
        }

        for (item in list){
            //TODO okabe 本来はこんな泥臭いことやらない。UIを自由に使いこなせるまで。
            val seedLayout = LinearLayout(this@MySeedActivity).apply {
                setOnClickListener{
                    AlertDialog.Builder(this@MySeedActivity).apply {
                        setTitle("Seed Info")
                        setMessage("TITLE: ${item.seedTitle}\nURL:${item.seedUrl}\nKeys: ${item.keySteries}")
                        setPositiveButton("OK"){_,_ ->
                        }
//                            setNegativeButton("RETURN"){_,_->
//                                finish()
//                            }
                        show()
                    }
                }
                layoutParams = ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                orientation = LinearLayout.VERTICAL
            }
            val titleText = TextView(this@MySeedActivity).apply {

                layoutParams = ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                gravity = Gravity.LEFT
                text = item.seedTitle
                setTextColor(Color.BLACK)
                setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24F)
            }
            val authorText = TextView(this@MySeedActivity).apply {

                layoutParams = ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                gravity = Gravity.RIGHT
                text = "created: ${item.createdTime}\nupdated: ${item.updatedTime}"
                setTextColor(Color.DKGRAY)
                setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12F)
            }
            seedLayout.addView(titleText)
            seedLayout.addView(authorText)
            mySeedList.addView(seedLayout)
        }
    }

}

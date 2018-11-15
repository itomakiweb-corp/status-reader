package com.itomakiweb.android.statusReader.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.itomakiweb.android.statusReader.R
import com.itomakiweb.android.statusReader.model.UserAuthManager
import com.itomakiweb.android.statusReader.service.Seed
import com.itomakiweb.android.statusReader.service.StatusReaderApiClient
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

    }

    override fun onStart() {
        super.onStart()
        try {
            getSeedAll()
        }
        catch (e: Throwable){
            AlertDialog.Builder(this@HomeActivity)
                    .setTitle("Get Seeds")
                    .setMessage(e.message).show()
        }
    }

    private fun getSeedAll(){
        SeedList.removeAllViews()
        StatusReaderApiClient.Instance.getAllSeeds().enqueue(object : Callback<List<Seed>> {
            override fun onFailure(call: Call<List<Seed>>?, t: Throwable?) {

                AlertDialog.Builder(this@HomeActivity)
                        .setTitle("Get Seeds")
                        .setMessage("failed.")
                        .show()
                progressBar_getSeedAll.visibility = View.GONE
            }

            override fun onResponse(call: Call<List<Seed>>?, response: Response<List<Seed>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        if(it.isEmpty()){
                            val tempText = TextView(this@HomeActivity).apply {
                                text = "no seeds."
                            }
                            SeedList.addView(tempText)
                        }
                        else{
                            try {
                                createSeedList(it)
                            }
                            catch (error: Exception){
                                val tempText = TextView(this@HomeActivity).apply {
                                    text = "${error?.message}"
                                }
                                SeedList.addView(tempText)
                            }
                        }
                    }
                }
                else{
                    AlertDialog.Builder(this@HomeActivity)
                            .setTitle("Get Seeds")
                            .setMessage("response is not Successful.").show()
                }
                progressBar_getSeedAll.visibility = View.GONE
            }
        })
    }

    private fun createSeedList(list: List<Seed>){
        for (item in list){
            //TODO okabe 本来はこんな泥臭いことやらない。UIを自由に使いこなせるまで。
            val seedLayout = LinearLayout(this@HomeActivity).apply {
                setOnClickListener{
                    val intent = Intent(this@HomeActivity, MainActivity::class.java)
                    intent.putExtra("seed", item)
                    startActivity(intent)
                }
                layoutParams = ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                orientation = LinearLayout.VERTICAL
            }
            val titleText = TextView(this@HomeActivity).apply {

                layoutParams = ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                gravity = Gravity.LEFT
                text = item.seedTitle
                setTextColor(Color.BLACK)
                setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24F)
            }
            val authorText = TextView(this@HomeActivity).apply {

                layoutParams = ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                gravity = Gravity.RIGHT
                text = "uploaded by: ${item.uploadUserName}"
                setTextColor(Color.DKGRAY)
                setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12F)
            }
            seedLayout.addView(titleText)
            seedLayout.addView(authorText)
            SeedList.addView(seedLayout)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.statusreader_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.menuitem_seed -> return menuSeed(item)
            R.id.menuitem_settings -> return menuAppSettings(item)
            R.id.menuitem_account -> return menuAccount(item)
            R.id.menuitem_status -> return menuUserStatus(item)

            else -> return super.onOptionsItemSelected(item)
        }
    }
    fun menuAppSettings(item: MenuItem): Boolean {
        return true
    }

    fun menuAccount(item: MenuItem): Boolean {
        UserAuthManager.signOut(this)
        val intent = Intent(this@HomeActivity, LaunchActivity::class.java)
        startActivity(intent)
        return true
    }

    fun menuUserStatus(item: MenuItem): Boolean {
        return true
    }

    fun menuSeed(item: MenuItem): Boolean {
        val intent = Intent(this@HomeActivity, MySeedActivity::class.java)
        startActivity(intent)
        return true
    }

}

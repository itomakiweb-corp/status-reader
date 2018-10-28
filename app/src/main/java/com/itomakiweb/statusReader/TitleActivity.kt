package com.itomakiweb.statusReader

import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.example.o4get.myapplication32.UserRequest
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.activity_title.*
import org.json.JSONException
import retrofit2.http.GET


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class TitleActivity : AppCompatActivity() {
    private var shouldSignin = true
    private var dialogTitle = "サインイン"
    private var message = ""
    private val AUTH_TYPE :String = "rerequest"
    private var callbackManager = CallbackManager.Factory.create()

    private val mHideHandler = Handler()
    private val mHidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        fullscreen_content.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
    private val mShowPart2Runnable = Runnable {
        // Delayed display of UI elements
        supportActionBar?.show()
        fullscreen_content_controls.visibility = View.VISIBLE
    }
    private var mVisible: Boolean = false
    private val mHideRunnable = Runnable { hide() }
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private val mDelayHideTouchListener = View.OnTouchListener { _, _ ->
        if (AUTO_HIDE) {
            delayedHide(AUTO_HIDE_DELAY_MILLIS)
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mVisible = true

        // Set up the user interaction to manually show or hide the system UI.
        fullscreen_content.setOnClickListener { toggle() }


        try{
            var token = AccessToken.getCurrentAccessToken()
            if(token == null){
                shouldSignin = true
                message = "サインインが必要です。"
            }
            else{
                if (token.isExpired){
                    shouldSignin = true
                    message = "サインインがタイムアウトしています。"
                }
                else{
                    shouldSignin = false
                    message = "サインイン済みです。"
                }
            }

        }
        catch (ex : Exception){
            shouldSignin = true
            message = "サインインが必要です。"
        }

        if(!shouldSignin){
            toHome()
        }
        else{
            AlertDialog.Builder(this@TitleActivity)
                    .setTitle(dialogTitle)
                    .setMessage(message).show()

            //TODO okabe デフォルトで承認されているらしい"default"が拒否される。"publicprofile"は要求不要。
            //login_button.setReadPermissions("default")
            LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {

                    AlertDialog.Builder(this@TitleActivity)
                            .setTitle("Signin")
                            .setMessage("token: " + result?.accessToken?.token).show()

                    toHome()
                }

                override fun onCancel() {
                    AlertDialog.Builder(this@TitleActivity)
                            .setTitle("Signin")
                            .setMessage("failed.").show()
                }

                override fun onError(error: FacebookException?) {
                    AlertDialog.Builder(this@TitleActivity)
                            .setTitle("Signin Error")
                            .setMessage(error?.message + ": " + error?.cause).show()
                }
            })

        }

    }

    fun toHome(){
        //TODO okabe "email"取得には許可が必要。
        val params = Bundle()
        params.putString("fields", "picture,name,id,email,permissions")

        val request = GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",//ME_ENDPOINT,
                params,
                HttpMethod.GET,
                GraphRequest.Callback { response ->
                    try {
                        val userObj = response.jsonObject ?: return@Callback
                        if(userObj.has("email")){
                            var em = userObj["email"]
                        }

                        val intent = Intent(this@TitleActivity, HomeActivity::class.java)
                        intent.putExtra("id",userObj["id"]?.toString())
                        intent.putExtra("name",userObj["name"]?.toString())
                        startActivity(intent)
                        finish()
                    } catch (e: JSONException) {
                        // Handle exception ...
                        AlertDialog.Builder(this@TitleActivity)
                                .setTitle("Signin Error")
                                .setMessage(e?.message + ": " + e?.cause).show()
                    }

                }
        )

        try{
            request.executeAsync()
        }
        catch (e : Exception){
            AlertDialog.Builder(this@TitleActivity)
                    .setTitle("Request Error")
                    .setMessage(e?.message + ": " + e?.cause).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100)
    }

    private fun toggle() {
        if (mVisible) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        // Hide UI first
        supportActionBar?.hide()
        fullscreen_content_controls.visibility = View.GONE
        mVisible = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable)
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun show() {
        // Show the system bar
        fullscreen_content.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        mVisible = true

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable)
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    /**
     * Schedules a call to hide() in [delayMillis], canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Int) {
        mHideHandler.removeCallbacks(mHideRunnable)
        mHideHandler.postDelayed(mHideRunnable, delayMillis.toLong())
    }

    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private val AUTO_HIDE_DELAY_MILLIS = 3000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private val UI_ANIMATION_DELAY = 300
    }
}

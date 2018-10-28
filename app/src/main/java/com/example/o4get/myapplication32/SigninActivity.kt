package com.example.o4get.myapplication32

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.itomakiweb.statusReader.R

import kotlinx.android.synthetic.main.activity_signin.*
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.FacebookCallback
import com.facebook.login.LoginManager
import kotlinx.android.synthetic.main.content_signin.*
import retrofit2.http.GET
import com.facebook.GraphRequest




class SigninActivity : AppCompatActivity() {
    private var shouldSignin = true
    private var dialogTitle = "サインイン"
    private var message = ""
    private val AUTH_TYPE :String = "rerequest"
    private var callbackManager = CallbackManager.Factory.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        setSupportActionBar(toolbar)

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

        AlertDialog.Builder(this@SigninActivity)
                .setTitle(dialogTitle)
                .setMessage(message).show()

        //TODO okabe デフォルトで承認されているらしい"default"が拒否される。"publicprofile"は要求不要。
        //login_button.setReadPermissions("default")


        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult?) {

                AlertDialog.Builder(this@SigninActivity)
                        .setTitle("Signin")
                        .setMessage("token: " + result?.accessToken).show()

                var ur = UserRequest()
            }

            override fun onCancel() {
                AlertDialog.Builder(this@SigninActivity)
                        .setTitle("Signin")
                        .setMessage("failed.").show()
            }

            override fun onError(error: FacebookException?) {
                AlertDialog.Builder(this@SigninActivity)
                        .setTitle("Signin Error")
                        .setMessage(error?.message + ": " + error?.cause).show()
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

}


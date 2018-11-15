package com.itomakiweb.android.statusReader.view

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.itomakiweb.android.statusReader.R
import com.itomakiweb.android.statusReader.model.StatusReaderAuthType
import com.itomakiweb.android.statusReader.model.UserAuthManager
import com.itomakiweb.android.statusReader.service.StatusReaderApiClient
import com.itomakiweb.android.statusReader.service.UserData
import kotlinx.android.synthetic.main.activity_title.*
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        buttonSubmit.setOnClickListener {
            val email = editText.text.toString()
            if(email == ""){

            }
            else{
                createUserData(authType = StatusReaderAuthType.Itomaki, userId = email, userName = email)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        //TODO okabe デフォルトで承認されているらしい"default"が拒否される。"publicprofile"は要求不要。
        //login_button.setReadPermissions("default")
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {

                AlertDialog.Builder(this@TitleActivity).apply {
                    setTitle("Signin")
                    setMessage("OK!")
                    setPositiveButton("OK"){_,_ -> toHome()}
                    show()
                }
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

    fun getFacebookUserInfo(){
        //TODO okabe "email"取得には許可が必要。
        val params = Bundle()
        params.putString("fields", "picture,name,id,permissions")

        val request = GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",//ME_ENDPOINT,
                params,
                HttpMethod.GET,
                GraphRequest.Callback { response ->
                    try {
                        val userObj = response.jsonObject ?: return@Callback
                        var name = if(userObj.has("name")) userObj["name"].toString() else ""
                        var id = if(userObj.has("id")) userObj["id"].toString() else ""
                        createUserData(authType = StatusReaderAuthType.Facebook, userId = id, userName = name)

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


    fun createUserData(authType: StatusReaderAuthType, userId: String, userName: String){
        val udata = UserData(userId= userId,authType = authType.value, userName = userName)
        try {
            var res = StatusReaderApiClient.Instance.createUserData(udata).enqueue(object : Callback<UserData> {
                override fun onFailure(call: Call<UserData>?, t: Throwable?) {

                    AlertDialog.Builder(this@TitleActivity)
                            .setTitle("Send UserData")
                            .setMessage("failed.")
                            .show()
                }

                override fun onResponse(call: Call<UserData>?, response: Response<UserData>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            AlertDialog.Builder(this@TitleActivity).apply {
                                setTitle("Create UserData")
                                setMessage("success.")
                                setPositiveButton("OK"){_,_ ->
                                    UserAuthManager.saveAuth(context, authType, it.userId, it.userName)
                                    toHome()
                                }
                                show()
                            }

                        }
                    }
                    else{
                        AlertDialog.Builder(this@TitleActivity)
                                .setTitle("Create UserData")
                                .setMessage("failed...").show()
                    }
                }
            })
        }
        catch (e: Throwable){
            AlertDialog.Builder(this@TitleActivity)
                    .setTitle("Send Data")
                    .setMessage(e.message).show()
        }
    }

    fun toHome(){
        val intent = Intent(this@TitleActivity, HomeActivity::class.java)
//        intent.putExtra("id",userObj["id"]?.toString())
//        intent.putExtra("name",userObj["name"]?.toString())
        startActivity(intent)
        finish()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }


}

package com.itomakiweb.android.statusReader.model

import android.content.Context
import android.preference.PreferenceManager
import com.facebook.AccessToken
import java.lang.Exception

object UserAuthManager {
    var message = ""
    var CurrentAuthType = StatusReaderAuthType.NONE
    var CurrentUserId = ""
    var CurrentUserName = ""

    private val pref_title = "USER_INFO"
    private val pref_authType = "AUTH_TYPE"
    private val pref_userId = "USER_ID"
    private val pref_userName = "USER_NAME"

    fun ShouldSignin(context: Context) :Boolean{
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        val authType = pref.getString(pref_authType, StatusReaderAuthType.NONE.value)

        var result = when(authType){
            StatusReaderAuthType.NONE.value -> {true}
            StatusReaderAuthType.Itomaki.value -> {
                val userId = pref.getString(pref_userId, "")
                val userName = pref.getString(pref_userName, "")
                if(userId == "" || userName == ""){
                    message = "サインインが必要です。"
                    true
                }
                else{
                    CurrentAuthType = StatusReaderAuthType.Itomaki
                    CurrentUserId = userId
                    CurrentUserName = userName
                    message = "サインイン済みです。"
                    false
                }
            }
            StatusReaderAuthType.Facebook.value -> {
                var facebookToken = AccessToken.getCurrentAccessToken()
                if(facebookToken == null){
                    message = "サインインが必要です。"
                    true
                }
                else{
                    if (facebookToken.isExpired){
                        message = "サインインがタイムアウトしています。"
                        true
                    }
                    else{
                        CurrentAuthType = StatusReaderAuthType.Facebook
                        message = "サインイン済みです。"
                        false
                    }
                }
            }
            StatusReaderAuthType.Google.value -> {true}
            StatusReaderAuthType.Twitter.value -> {true}
            else -> true
        }
        return result
    }

    fun SaveAuth(context: Context, authType:StatusReaderAuthType, userId: String, userName: String){
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        var editor = pref.edit()
        try {
            editor.putString(pref_authType, authType.value)
            editor.putString(pref_userId, userId)
            editor.putString(pref_userName, userName)
            editor.apply()

            CurrentAuthType = authType
            CurrentUserId = userId
            CurrentUserName = userName
        }
        catch (error: Exception){
            throw error
        }
    }
}

enum class StatusReaderAuthType(val value: String){
    NONE(""),Itomaki("Itomaki"),Facebook("Facebook"),Twitter("Twitter"),Google("Google")
}

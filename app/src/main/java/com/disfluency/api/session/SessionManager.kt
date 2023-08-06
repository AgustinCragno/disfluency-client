package com.disfluency.api.session

import android.content.Context
import android.content.SharedPreferences
import com.disfluency.R

object SessionManager {
    private const val USER_TOKEN = "user_refresh_token"

    private lateinit var prefs: SharedPreferences

    private var accessToken: String? = null

    fun initialize(context: Context){
        prefs = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    }

    fun saveRefreshToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun getRefreshToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun saveAccessToken(token: String){
        accessToken = token
    }

    fun getAccessToken(): String? {
        return accessToken
    }
}
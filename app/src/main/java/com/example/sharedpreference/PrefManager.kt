package com.example.sharedpreference

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import kotlin.math.log

class PrefManager private constructor(context: Context){
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME,MODE_PRIVATE)

    companion object {
        private const val PREFS_FILENAME = "AuthAppPrefs"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_USERNAME = "username"

        @Volatile
        private var instance: PrefManager? = null

        fun getInstance(context: Context): PrefManager {
            return instance ?: synchronized(this) {
                instance ?: PrefManager(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        editor.apply()
        Log.e("setLoggedIn", "fungsi setLoggedIn dipanggil")
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun saveUsername(username: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USERNAME, username)
        editor.apply()
    }

    fun getUsername(): String {
        return sharedPreferences.getString(KEY_USERNAME, "").orEmpty()
    }

    fun clear () {
        sharedPreferences.edit().also {
            it.clear()
            it.apply()
        }
    }
// sama saja dengan fungsi clear dibawah
//    fun clear() {
//        val editor = sharedPreferences.edit()
//        editor.clear()
//        editor.apply()
//    }

}
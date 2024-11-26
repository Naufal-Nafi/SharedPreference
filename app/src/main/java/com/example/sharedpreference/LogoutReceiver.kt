package com.example.sharedpreference

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class LogoutReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        p0?.let {
            val prefManager = PrefManager.getInstance(it)
            prefManager.setLoggedIn(false)

            val intent = Intent("com.example.ACTION_LOGOUT")
            LocalBroadcastManager.getInstance(it).sendBroadcast(intent)
        }
    }
}
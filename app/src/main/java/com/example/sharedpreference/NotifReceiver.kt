package com.example.sharedpreference

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class NotifReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        val msg = p1?.getStringExtra("MESSAGE")
        if (msg != null) {
            Toast.makeText(p0, msg, Toast.LENGTH_LONG).show()
        }
    }
}
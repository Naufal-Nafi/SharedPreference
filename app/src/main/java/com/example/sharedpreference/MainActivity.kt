package com.example.sharedpreference

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.sharedpreference.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var prefManager: PrefManager
    private val usernameData = "tes"
    private val passwordData = "123"
    val channelId = "TEST_NOTIF"
    val notifId = 90

    private val logoutReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            checkLoginstatus()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)
        checkLoginstatus()


        val filter = IntentFilter("com.example.ACTION_LOGOUT")
        LocalBroadcastManager.getInstance(this).registerReceiver(logoutReceiver, filter)


        with(binding) {
            btnLogin.setOnClickListener {
                val username = edtUsername.text.toString()
                val password = edtPassword.text.toString()
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this@MainActivity, "Mohon isi semua data", Toast.LENGTH_SHORT).show()
                } else {
                    if (username == usernameData && password == passwordData) {
                        prefManager.setLoggedIn(true)
                        prefManager.saveUsername(username)
                        checkLoginstatus()
                    } else {
                        Toast.makeText(this@MainActivity, "Username atau password salah", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            btnLogout.setOnClickListener {
                prefManager.setLoggedIn(false)
                checkLoginstatus()
            }

            btnClear.setOnClickListener {
                prefManager.clear()
                checkLoginstatus()
            }

            val notifManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            btnNotif.setOnClickListener {
                val notifImg = BitmapFactory.decodeResource(resources,R.drawable.img)
                val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PendingIntent.FLAG_IMMUTABLE
                }
                else {
                    0
                }
//                val intent = Intent(this@MainActivity,
//                    NotifReceiver::class.java).putExtra("MESSAGE", "Baca selengkapnya ...")
                val intent = Intent(this@MainActivity, MainActivity::class.java)

//                val pendingIntent = PendingIntent.getBroadcast(
//                    this@MainActivity,
//                    0,
//                    intent,
//                    flag
//                )
                val pendingIntent = PendingIntent.getActivity(
                    this@MainActivity,
                    0,
                    intent,
                    flag
                )

                val logoutIntent = Intent(this@MainActivity, LogoutReceiver::class.java)
                val logoutPendingIntent = PendingIntent.getBroadcast(
                    this@MainActivity,
                    0,
                    logoutIntent,
                    flag
                )



                val builder = NotificationCompat.Builder(this@MainActivity, channelId)
                    .setSmallIcon(R.drawable.baseline_notifications_24)
                    .setContentTitle("Notifku")
                    .setContentText("Hello World!") // Isi pesan bebas
//                    .setStyle(
//                        NotificationCompat.BigPictureStyle().bigPicture(notifImg)
//                    )
                    .addAction(
                        R.drawable.baseline_notifications_24, // Ikon untuk tombol
                        "Logout", // Teks tombol
                        logoutPendingIntent
                    )
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
//                    .setContentIntent(pendingIntent)


                val notifManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val notifChannel = NotificationChannel(
                        channelId,
                        "Notifku",
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                    with(notifManager) {
                        createNotificationChannel(notifChannel)
                        notify(notifId,builder.build())
                    }
                } else {
                    notifManager.notify(notifId, builder.build())
                }
            }
        }
    }

    fun checkLoginstatus() {
        val isLoggedIn = prefManager.isLoggedIn()
        if (isLoggedIn) {
            binding.llLogged.visibility = View.VISIBLE
            binding.llLogin.visibility = View.GONE
        } else {
            binding.llLogged.visibility = View.GONE
            binding.llLogin.visibility = View.VISIBLE
        }
        Log.e("checkLoginStatus", "fungsi checkLoginStatus dipanggil")
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(logoutReceiver)
    }

}
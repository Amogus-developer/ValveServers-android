package com.example.tf2servers.service

import android.app.IntentService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import com.example.tf2servers.udp.SourceEngineClient

class MyIntentService : IntentService(MyIntentService::class.java.simpleName) {

    private val server = SourceEngineClient()
    private val mainExecutor = Handler(Looper.getMainLooper())

    private var cycle = true

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onHandleIntent(intent: Intent?) {
        val tm = intent!!.getIntExtra("time", 0)

        val address = intent.getStringExtra("address")
        val port = intent.getIntExtra("port", 0)
        val players = intent.getIntExtra("players", 0)
        val notificationManager = NotificationManagerCompat.from(this)
        val builder = makeMessage(CHANNEL_ID, "Server request", "Service running")
        startForeground(NOTIFY_ID, builder.build())

        while (cycle) {
            val info = server.getInfo(address.toString(), port)
            val builder = makeMessage(
                CHANNEL_ID2,
                info.serverName,
                "players: ${info.playerCount}/${info.maxPlayers}"
            )
            if (info.playerCount >= players) {
                cycle = false
                mainExecutor.post{ Toast.makeText(applicationContext,
                    info.serverName+": "+info.playerCount+"/"+info.maxPlayers,
                    Toast.LENGTH_SHORT).show()}

                checkPermission(CHANNEL_ID2, NOTIFY_ID2, NOTIFY_ID2)
                notificationManager.notify(NOTIFY_ID2, builder.build())
                notificationManager.deleteNotificationChannel(CHANNEL_ID)

            }
            Thread.sleep(tm.toLong())
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun makeMessage(id: String, title: String, description: String,
                            image: Int = com.example.tf2servers.R.drawable.icon): Notification.Builder {

        val channel = NotificationChannel(id, id, NotificationManager.IMPORTANCE_LOW)
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        return Notification.Builder(this, id).setContentText(description).setContentTitle(title)
            .setSmallIcon(image)
    }
    companion object{
        const val CHANNEL_ID = "Server request ID"
        const val NOTIFY_ID = 1001

        const val CHANNEL_ID2 = "Message ID"
        const val NOTIFY_ID2 = 101
    }
}
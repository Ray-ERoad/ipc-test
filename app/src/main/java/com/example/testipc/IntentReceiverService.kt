package com.example.testipc

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import java.util.*
import kotlin.collections.HashMap
import kotlin.concurrent.thread

class IntentReceiverService : Service() {
    private var hasSetup = false
    private var dictionary: HashMap<String, Int> = hashMapOf<String, Int>()
    private var lastMessage = ""
    private lateinit var iotMessenger: IotMessenger

    override fun onCreate() {
        super.onCreate()
        Log.d("IntentReceiverService", "=================Intent Receiver Service Started=================")
        iotMessenger = IotMessenger()
        setup()
        continuouslyUploadData()
    }

    private fun continuouslyUploadData() {
        Thread {
            while (true) {
                if (lastMessage.isNotEmpty()) {
                    iotMessenger.addMessage(lastMessage)
                    Thread.sleep(10000) // sleep 10s
                }
            }
        }.start()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val data = intent?.getStringExtra("Data")

        val value = data!!.split(": ")
        val name = value.first()
        val count = value.last().toInt()

        dictionary[name]?.let {
            if (count - it != 1) {
                Log.e("IntentReceiverService", "Message lost. Last counter: $it, this counter: $count")
            }
        }

        dictionary[name] = count

        lastMessage = data
        Log.d("IntentReceiverService", "Data: $data")

//        if (count % 1111 == 0) {
//            stopSelf()
//        }
        setup()
        return START_STICKY
    }

    /**
     * This needs to be called by both onCreated and onStartCommand methods.
     * Did a lot of researches, calling by two places seems to be the safest to do.
     * Looks like in some circumstances, only one of the method(onCreated or onStartCommand) will be triggere, although it's not documented by Android officially.
     * One of the reference: https://stackoverflow.com/a/58528446
     */
    private fun setup() {
        if(hasSetup) return
        hasSetup = true

        runForeground()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun runForeground() {
        // This check is for just incase we change this to a lower version to prevent it from crashing.
        if (Build.VERSION.SDK_INT >= 26) {
            val notification = createNotification()
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    private fun createNotification() : Notification {
        val importantMessage = "Please keep this notification, otherwise functions will not work correctly."
        val channelId = "Ipc_Test_Notification_Channel" // please don't change this once the app is published
        val channelName = "Keep ${this.javaClass.simpleName} alive"
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = importantMessage

        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)

        val notification = Notification.Builder(this, channelId)
            .setContentTitle("Intent receiver service is running")
            .setContentText(importantMessage)
            .setSmallIcon(R.drawable.ic_lock_idle_lock)
            .build()
        return notification
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object{
        private const val NOTIFICATION_ID = 10001
    }
}
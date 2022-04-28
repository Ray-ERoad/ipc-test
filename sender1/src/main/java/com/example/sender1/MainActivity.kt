package com.example.sender1

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    private var count: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Thread {
            while(true) {
                for (i in 0..200) {
                    count++
//                    startService()
                    sendBroadcast()
                }

                Thread.sleep(1000)
            }
        }.start()
    }

    private fun startService() {
        val componentName = ComponentName("com.example.testipc", "com.example.testipc.IntentBroadcastReceiver")
        val intent = Intent()
        intent.component = componentName
        intent.putExtra("Data", "App1: $count")
        this.startForegroundService(intent)
    }

    private fun sendBroadcast() {
        Intent().also { intent ->
            intent.component = ComponentName("com.example.testipc", "com.example.testipc.IntentBroadcastReceiver")
            intent.setAction("com.test.intent.action.DEFAULT")
            intent.putExtra("Data", "App1: $count")
            sendBroadcast(intent)
        }
    }
}
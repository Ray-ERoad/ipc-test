package com.example.sender3

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
                for (i in 0..100) {
                    count++
                    Intent().also { intent ->
                        intent.component = ComponentName("com.example.testipc", "com.example.testipc.IntentBroadcastReceiver")
                        intent.setAction("com.test.intent.action.DEFAULT")
                        intent.putExtra("Data", "App3: $count")
                        sendBroadcast(intent)
                    }
                }

                Thread.sleep(5000)
            }
        }.start()
    }
}
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
                    val componentName = ComponentName("com.example.testipc", "com.example.testipc.IntentReceiverService")
                    val intent = Intent()
                    intent.component = componentName
                    intent.putExtra("Data", "App3: $count")
                    this.startForegroundService(intent)
                }

                Thread.sleep(5000)
            }
        }.start()
    }
}
package com.example.sender1

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
                    Intent().also { intent ->
                        intent.setAction("com.test.intent.action.DEFAULT")
                        intent.putExtra("Data", "App1: $count")
                        sendBroadcast(intent)
                    }
                }

                Thread.sleep(1000)
            }
        }.start()
    }
}
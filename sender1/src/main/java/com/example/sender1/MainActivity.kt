package com.example.sender1

import android.os.*
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.ipcmessenger.IpcMessenger
import com.example.ipcmessenger.MessengerCallback

class MainActivity : AppCompatActivity() {
    private var count: Int = 0
    private lateinit var ipcMessenger: IpcMessenger
    private var isMessengerBound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ipcMessenger = IpcMessenger(this)
        sendMessage()
        findViewById<Button>(R.id.send_message_button).setOnClickListener {
            useMessenger()
        }
    }

    private fun useMessenger() {
        ipcMessenger.bindUsingMessage(object: MessengerCallback {
            override fun onConnected() {
                Log.d("MainActivity", "App1 messenger connected")
                isMessengerBound = true
            }

            override fun onDisconnected() {
                Log.d("MainActivity", "App1 messenger disconnected")
                isMessengerBound = false
            }

            override fun onReceived(message: String) {
                Log.d("MainActivity", "App1 messenger received message: $message")
            }
        })
    }

    fun sendMessage() {
        Thread {
            while (true) {
                if (isMessengerBound == false) continue

                for (i in 1 .. 200) {
                    count++
                    ipcMessenger.sendMessengerMessage("App1: $count")
                }
                Thread.sleep(1000)
            }
        }.start()
    }
}


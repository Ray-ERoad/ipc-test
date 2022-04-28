package com.example.sender2

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import com.example.protocol.IRemoteService

class MainActivity : AppCompatActivity() {
    private var count: Int = 0
    private var iRemoteService: IRemoteService? = null
    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            iRemoteService = IRemoteService.Stub.asInterface(service)
            startToSend()
        }

        override fun onServiceDisconnected(className: ComponentName) {
            iRemoteService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent()
        intent.component = ComponentName("com.example.testipc", "com.example.protocol.RemoteService")
        intent.action = IRemoteService::class.java.name
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        unbindService(mConnection)
    }

    private fun startToSend() {
        Thread {
            while(true) {
                for (i in 0..300) {
                    count++
                    iRemoteService?.basicTypes(count,0, true, 0.0f, 0.0, "App2: $count")
                }
                Thread.sleep(3000)
            }
        }.start()
    }
}
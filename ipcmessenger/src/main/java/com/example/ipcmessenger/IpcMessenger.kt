package com.example.ipcmessenger

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.lang.Exception

class IpcMessenger(private val context: Context) {
    private var remoteMessenger: Messenger? = null
    private var isBound = false
    private var clientMessenger: Messenger? = null
    private var messengerCallback: MessengerCallback? = null

    private val componentName: ComponentName by lazy {
        ComponentName("com.example.testipc", "com.example.testipc.IntentBroadcastReceiver")
    }

    fun startService(message: String) {
        val intent = Intent()
        intent.component = componentName
        intent.putExtra("Data", message)
        context.startForegroundService(intent)
    }

    fun sendBroadcast(message: String)  {
        Intent().also { intent ->
            intent.component = componentName
            intent.setAction("com.test.intent.action.DEFAULT")
            intent.putExtra("Data", message)
            context.sendBroadcast(intent)
        }
    }

    fun bindUsingMessage(messengerCallback: MessengerCallback) {
        this.messengerCallback = messengerCallback
        bindService()
    }

    fun sendMessengerMessage(message: String) {
        val msg = Message.obtain()
        msg.replyTo = clientMessenger
        val bundle = Bundle()
        bundle.putString("Data", message);
        msg.setData(bundle);

        try {
            remoteMessenger?.send(msg);
        } catch (e: Exception) {
            Log.d("IpcMessenger", "Exception occurred when sending the message: $e")
        }
    }

    private fun bindService() {
        val intent = Intent()
        intent.action = "android.intent.action.MESSENGER"
        intent.setPackage("com.example.testipc")
        context.bindService(intent, myConnection, AppCompatActivity.BIND_AUTO_CREATE)
    }

    private val myConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            remoteMessenger = Messenger(service)
            clientMessenger = Messenger(ClientHandler(messengerCallback))
            isBound = true
            messengerCallback?.onConnected()
        }

        override fun onServiceDisconnected(
            className: ComponentName
        ) {
            messengerCallback?.onDisconnected()
            messengerCallback = null
            remoteMessenger = null
            isBound = false
        }
    }

    class ClientHandler(private val messengerCallback: MessengerCallback?): Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            messengerCallback?.onReceived("${msg.data.getString("Reply")}")
        }
    }
}

interface MessengerCallback {
    fun onConnected()
    fun onDisconnected()
    fun onReceived(message: String)
}
package com.example.testipc

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log


class MessengerService: Service() {

    private val messenger = Messenger(ServiceHandler())

    override fun onBind(p0: Intent?): IBinder? {
        return messenger.binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        mClient = null
        return super.onUnbind(intent)
    }

    fun sendMessage() {
        if (null == mClient) {
            Log.d("service", "client is null")
            return
        }
        try {
            val message: Message = Message.obtain(null, FIND_DEVICE)
            mClient!!.send(message)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    class ServiceHandler: Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            Log.d("MessengerService", "${msg.data.getString("Data")}")
//            mClient = msg.replyTo
//            val message = Message.obtain()
//            val bundle = Bundle()
//            bundle.putString("Reply", "Reply: ${msg.data.getString("Data")}")
//            message.data = bundle
//            mClient?.send(message)
        }
    }

    companion object {
        private const val SEND_MESSENGER = 0
        private const val CONFIG_NET = 1
        private const val CANCEL = 2
        private const val FIND_DEVICE = 10
        private var mClient: Messenger? = null
    }
}
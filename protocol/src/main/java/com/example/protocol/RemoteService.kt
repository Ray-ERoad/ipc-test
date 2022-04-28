package com.example.protocol

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class RemoteService : Service() {

    private val binder = object : IRemoteService.Stub() {
        override fun basicTypes(
            anInt: Int,
            aLong: Long,
            aBoolean: Boolean,
            aFloat: Float,
            aDouble: Double,
            aString: String?
        ) {
            Log.d("RemoteService", "name: $aString, count: $anInt")
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("RemoteService", "=================RemoteService Started=================")
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d("RemoteService", "=================RemoteService onBind=================")
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d("RemoteService", "=================RemoteService onUnbind=================")
        return super.onUnbind(intent)
    }

}
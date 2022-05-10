package com.example.testipc

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class InternalBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("InternalBroadcastReceiver", "intent: ${intent?.extras?.get("Data")}")
    }
}
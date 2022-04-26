package com.example.testipc

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class IntentBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("IntentBroadcastReceiver", "intent: ${intent?.extras?.get("Data")}")
    }
}
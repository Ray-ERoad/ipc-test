package com.example.testipc

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log

class IntentBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("IntentBroadcastReceiver", "intent: ${intent?.extras?.get("Data")}")
        if (context == null || intent == null) return

        startService(context, intent)
    }

    private fun startService(context: Context, intent: Intent) {
        val componentName = ComponentName("com.example.testipc", "com.example.testipc.IntentBroadcastReceiver")
        val newIntent = Intent()
        newIntent.component = componentName
        newIntent.putExtra("Data", intent.getStringExtra("Data"))
        context.startForegroundService(intent)
    }
}
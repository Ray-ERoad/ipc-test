package com.example.testipc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // These are needed to use the 2 libraries.
        com.eroad.security.initialize(this.applicationContext)
        com.eroad.aws.initialize(this.applicationContext)
    }
}
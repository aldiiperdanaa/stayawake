package com.aldiperdana.mobilestayawake

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aldiperdana.mobilestayawake.ui.LiveActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {
    private lateinit var startLiveButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startLiveButton = findViewById(R.id.startLive)
        startLiveButton.setOnClickListener {
            val serviceIntent = Intent(this, LiveActivity::class.java)
            startService(serviceIntent)
        }
    }
}
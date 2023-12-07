package com.aldiperdana.mobilestayawake

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aldiperdana.mobilestayawake.databinding.ActivityMainBinding
import com.aldiperdana.mobilestayawake.ui.LiveActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val startLiveButton: FloatingActionButton = binding.startLive
        startLiveButton.setOnClickListener {
            val intent = Intent(this, LiveActivity::class.java)
            startActivity(intent)
        }
    }
}
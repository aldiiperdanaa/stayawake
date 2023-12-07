package com.aldiperdana.mobilestayawake

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aldiperdana.mobilestayawake.databinding.ActivityOnboardingScreenBinding
import com.aldiperdana.mobilestayawake.ui.LiveActivity

class OnboardingScreenActivity : AppCompatActivity() {
    private lateinit var binding:ActivityOnboardingScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGetStarted.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
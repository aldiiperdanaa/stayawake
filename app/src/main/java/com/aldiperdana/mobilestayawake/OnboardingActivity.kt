package com.aldiperdana.mobilestayawake

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.aldiperdana.mobilestayawake.databinding.ActivityOnboardingBinding
import com.aldiperdana.mobilestayawake.factory.ViewModelFactory

class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding:ActivityOnboardingBinding
    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this, ViewModelFactory.getInstance(this)).get(MainViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        mainViewModel.getSession().observe(this) { user ->
            if (user.isLogin) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }

        }

        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.btnGetStarted.setOnClickListener{
            val intent = Intent(this, LoginRegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
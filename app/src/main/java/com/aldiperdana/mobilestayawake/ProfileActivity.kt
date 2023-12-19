package com.aldiperdana.mobilestayawake

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.aldiperdana.mobilestayawake.databinding.ActivityLoginBinding
import com.aldiperdana.mobilestayawake.databinding.ActivityProfileBinding
import com.aldiperdana.mobilestayawake.factory.ViewModelFactory

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this, ViewModelFactory.getInstance(this)).get(MainViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }

        val logout = binding.btnLogout
        logout.setOnClickListener{
            mainViewModel.logout()
            val intent = Intent(this, SplashActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
package com.aldiperdana.mobilestayawake

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.aldiperdana.mobilestayawake.factory.ViewModelFactory

class SplashActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this, ViewModelFactory.getInstance(this)).get(MainViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler().postDelayed({
            mainViewModel.getSession().observe(this) { user ->
                if (user.isLogin) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }

            }
            val intent = Intent(this, OnboardingActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}
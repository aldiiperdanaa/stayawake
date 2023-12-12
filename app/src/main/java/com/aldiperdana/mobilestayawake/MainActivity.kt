package com.aldiperdana.mobilestayawake

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.aldiperdana.mobilestayawake.databinding.ActivityMainBinding
import com.aldiperdana.mobilestayawake.ui.LiveActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigation = findViewById(R.id.bottomNavigation)
        replace(HomeFragment())
        bottomNavigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.navigation_home->replace(HomeFragment())
                R.id.navigation_history->replace(HistoryFragment())
                R.id.navigation_community->replace(CommunityFragment())
                R.id.navigation_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }

        val startLiveButton: FloatingActionButton = binding.startLive
        startLiveButton.setOnClickListener {
            val intent = Intent(this, LiveActivity::class.java)
            startActivity(intent)
        }
    }

    private fun replace(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.navhost, fragment)
        fragmentTransaction.commit()
    }
}
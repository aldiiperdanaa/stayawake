package com.aldiperdana.mobilestayawake

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.aldiperdana.mobilestayawake.databinding.ActivityNewUserBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class NewUserActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNewUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottom_sheet: TextView = findViewById(R.id.tv_referral)
        bottom_sheet.setOnClickListener{
            val view: View = layoutInflater.inflate(R.layout.item_bottom_sheet_referral_code, null)
            val dialog = BottomSheetDialog(this)
            dialog.setContentView(view)
            dialog.show()
        }

        binding.role.setOnCheckedChangeListener{ group, checkedId ->
            when (checkedId) {
                R.id.personalRadioButton -> {
                    binding.btnGetStarted.setOnClickListener{
                        val intent = Intent(this, PersonalActivity::class.java)
                        startActivity(intent)
                    }
                }
                R.id.companyRadioButton -> {
                    binding.btnGetStarted.setOnClickListener{
                        val intent = Intent(this, CompanyActivity::class.java)
                        startActivity(intent)
                    }
                }
            }


        }



    }
}
package com.aldiperdana.mobilestayawake

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.aldiperdana.mobilestayawake.databinding.ActivityNewUserBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class NewUserActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNewUserBinding
    private lateinit var radioGroup: RadioGroup
    private lateinit var personalRadioButton: RadioButton
    private lateinit var companyRadioButton: RadioButton
    private lateinit var textViewPersonal: TextView
    private lateinit var textViewCompany: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        radioGroup = findViewById(R.id.radioGroup)
        personalRadioButton = findViewById(R.id.personalRadioButton)
        companyRadioButton = findViewById(R.id.companyRadioButton)
        textViewPersonal = findViewById(R.id.textViewPersonal)
        textViewCompany = findViewById(R.id.textViewCompany)

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                personalRadioButton.id -> {
                    textViewPersonal.setTextColor(resources.getColor(R.color.primary80))
                    textViewCompany.setTextColor(resources.getColor(R.color.textSecondary))
                }
                companyRadioButton.id -> {
                    textViewCompany.setTextColor(resources.getColor(R.color.primary80))
                    textViewPersonal.setTextColor(resources.getColor(R.color.textSecondary))
                }
            }
        }

        val bottom_sheet: TextView = findViewById(R.id.tv_referral)
        bottom_sheet.setOnClickListener{
            val view: View = layoutInflater.inflate(R.layout.item_bottom_sheet_referral_code, null)
            val dialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
            dialog.setContentView(view)
            dialog.show()
        }

        binding.radioGroup.setOnCheckedChangeListener{ group, checkedId ->
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
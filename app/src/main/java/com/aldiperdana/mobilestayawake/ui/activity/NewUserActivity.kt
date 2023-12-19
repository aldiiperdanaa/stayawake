package com.aldiperdana.mobilestayawake.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.aldiperdana.mobilestayawake.R
import com.aldiperdana.mobilestayawake.databinding.ActivityNewUserBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class NewUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewUserBinding
    private lateinit var radioGroup: RadioGroup
    private lateinit var personalRadioButton: RadioButton
    private lateinit var companyRadioButton: RadioButton
    private lateinit var textViewPersonal: TextView
    private lateinit var textViewCompany: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }

        val bottomSheet: TextView = findViewById(R.id.tv_referral)
        bottomSheet.setOnClickListener {
            val view: View = layoutInflater.inflate(R.layout.item_bottom_sheet_referral_code, null)
            val dialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
            dialog.setContentView(view)
            dialog.show()
        }

        radioGroup = findViewById(R.id.role)
        personalRadioButton = findViewById(R.id.personalRadioButton)
        companyRadioButton = findViewById(R.id.companyRadioButton)
        textViewPersonal = findViewById(R.id.textViewPersonal)
        textViewCompany = findViewById(R.id.textViewCompany)

        binding.btnContinue.setOnClickListener {
            when (radioGroup.checkedRadioButtonId) {
                R.id.personalRadioButton -> startActivity(Intent(this, PersonalActivity::class.java))
                R.id.companyRadioButton -> startActivity(Intent(this, CompanyActivity::class.java))
            }
        }

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
    }
}

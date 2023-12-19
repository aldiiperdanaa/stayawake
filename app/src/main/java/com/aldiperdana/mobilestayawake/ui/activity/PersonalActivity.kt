package com.aldiperdana.mobilestayawake.ui.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.aldiperdana.mobilestayawake.R
import com.aldiperdana.mobilestayawake.databinding.ActivityPersonalBinding
import java.util.Calendar

class PersonalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPersonalBinding
    var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }

        val dateOfBirthInput: EditText = findViewById(R.id.dateOfBrithInput)

        dateOfBirthInput.setOnClickListener {
            showDatePicker()
        }

        binding.btnContinue.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val formattedDate = String.format("%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year)

                val dateOfBirthInput: EditText = findViewById(R.id.dateOfBrithInput)
                dateOfBirthInput.setText(formattedDate)

                selectedDate = formattedDate
            },
            year,
            month,
            day
        )

        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

        datePickerDialog.show()
    }

}

package com.aldiperdana.mobilestayawake

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar
import com.aldiperdana.mobilestayawake.databinding.ActivityPersonalBinding

class PersonalActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPersonalBinding
    var selectedDate: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

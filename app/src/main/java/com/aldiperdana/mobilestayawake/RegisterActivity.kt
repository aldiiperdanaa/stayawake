package com.aldiperdana.mobilestayawake

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.aldiperdana.mobilestayawake.databinding.ActivityRegisterBinding
import com.aldiperdana.mobilestayawake.factory.ViewModelFactory
import com.aldiperdana.mobilestayawake.helper.ResultState
import com.aldiperdana.mobilestayawake.viewmodel.RegisterViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    private var selectedDate: String = ""
    var dateString : String = ""
    private var genderChoose : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fullNameEditText = binding.fullNameInput
        val birthPlaceEditText = binding.placeOfBirthInput
        val dateOfBirthInputEditText = binding.dateOfBrithInput
        val bloodTypeEditText = binding.bloodTypeInput
        val genderRadioGroup = binding.genderType
        val jobEditText = binding.jobTitleInput
        val telpEditText = binding.phoneNumberInput
        val emailEditText = binding.emailInput
        val passwordEditText = binding.passwordInput

        setupViewModel()

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }


        genderRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) {
                val selectedRadioButton: RadioButton = findViewById(checkedId)
                val selectedGender = selectedRadioButton.text.toString()
                genderChoose = selectedGender
            } else {

            }
        }

        binding.dateOfBrithInput.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(this@RegisterActivity,
                { view, year, monthOfYear, dayOfMonth ->
                    val selectedDate ="$dayOfMonth/${monthOfYear + 1}/$year"
                        dateOfBirthInputEditText.setText(selectedDate)
                     dateString = "$dayOfMonth/${monthOfYear + 1}/$year"
                }, year, month, dayOfMonth)
            datePickerDialog.show()
        }


        binding.btnRegister.setOnClickListener{
            val fullname = fullNameEditText.text.toString()
            val birthplace = birthPlaceEditText.text.toString()
            val selectedDate: Date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dateString)
            val bloodtype = bloodTypeEditText.text.toString()
            val gender = genderChoose
            val job = jobEditText.text.toString()
            val telp  = telpEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val kodeReferral = ""

            registerViewModel.register(
                fullname,
                birthplace,
                selectedDate,
                bloodtype,
                gender,
                job,
                telp,
                telp,
                email,
                password,
                kodeReferral
            )
        }

    }

    private fun setupViewModel(){
        val myFactory : ViewModelFactory = ViewModelFactory.getInstance(applicationContext)
        registerViewModel = ViewModelProvider(this,myFactory)[RegisterViewModel::class.java]

        registerViewModel.register.observe(this){
            when (it){
                is ResultState.Loading -> {

                }
                is ResultState.Success -> {
                    androidx.appcompat.app.AlertDialog.Builder(this,R.style.AlertDialogTheme).apply {
                        setTitle(getString(R.string.register_success))
                        setMessage(getString(R.string.message_create_acc))
                        setPositiveButton(getString(R.string.next)) { _, _ ->
                            val intent = Intent(this@RegisterActivity, LoginRegisterActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                        create()
                        show()
                    }

                }
                is ResultState.Error -> {
                    showToast(it.error)
                }
                else -> {
                    showToast(message = "Nothing")
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
    }


}
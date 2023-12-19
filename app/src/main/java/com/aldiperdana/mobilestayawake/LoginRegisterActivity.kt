package com.aldiperdana.mobilestayawake

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.aldiperdana.mobilestayawake.databinding.ActivityLoginRegisterBinding
import com.aldiperdana.mobilestayawake.factory.ViewModelFactory
import com.aldiperdana.mobilestayawake.helper.ResultState
import com.aldiperdana.mobilestayawake.viewmodel.LoginViewModel
import java.util.regex.Pattern

class LoginRegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginRegisterBinding
    private lateinit var loginViewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.registerLink.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val email = binding.emailInput
        val password = binding.passwordInput

        email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isValidEmail(s.toString())) {
                    email.error = "Email tidak valid"
                } else {
                    email.error = null
                }
            }
        })

        password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
        })

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.btnContinue.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()
            if (isValidEmail(email)) {
                loginViewModel.login(email, password)
            }else{
                Toast.makeText(this, " Login Gagal, Coba Lagi !", Toast.LENGTH_SHORT).show();
            }
        }
        setupViewModel()
    }
    private fun setupViewModel(){
        val myFactory : ViewModelFactory = ViewModelFactory.getInstance(applicationContext)
        loginViewModel = ViewModelProvider(this,myFactory)[LoginViewModel::class.java]

        loginViewModel.loginResponse.observe(this){
            when (it){
                is ResultState.Loading -> {
                    //showLoading(true)
                }
                is ResultState.Success -> {
                    //showLoading(false)
                    AlertDialog.Builder(this,R.style.AlertDialogTheme).apply {
                        setTitle(getString(R.string.login_expression))
                        setMessage(getString(R.string.message_login))
                        setPositiveButton(getString(R.string.next)) { _, _ ->
                            val intent = Intent(this@LoginRegisterActivity, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                        create()
                        show()
                    }
                }
                is ResultState.Error -> {
                    AlertDialog.Builder(this,R.style.AlertDialogTheme).apply {
                        setTitle(getString(R.string.fail_expression))
                        setMessage(getString(R.string.invalid_email))
                        create()
                        show()
                    }
                }
                else -> {
                    showToast(message = "")
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val pattern = Pattern.compile(emailPattern)
        return pattern.matcher(email).matches()
    }



}

package com.aldiperdana.mobilestayawake.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aldiperdana.mobilestayawake.helper.ResultState
import com.aldiperdana.mobilestayawake.repository.UserRepository
import com.aldiperdana.mobilestayawake.response.LoginResponse

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _login = MediatorLiveData<ResultState<LoginResponse>>()
    val loginResponse : LiveData<ResultState<LoginResponse>> = _login

    fun login(
        email : String,
        password : String){
        _isLoading.value = true
        val liveData = repository.login(email, password)
        _login.addSource(liveData){ result ->
            _login.value = result
        }
    }
}
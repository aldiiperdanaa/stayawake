package com.aldiperdana.mobilestayawake.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.aldiperdana.mobilestayawake.helper.ResultState
import com.aldiperdana.mobilestayawake.repository.UserRepository
import com.aldiperdana.mobilestayawake.response.RegisterResponse
import java.util.Date

class RegisterViewModel (private val userRepository: UserRepository): ViewModel() {

    private val _register = MediatorLiveData<ResultState<RegisterResponse>>()
    val register : LiveData<ResultState<RegisterResponse>> = _register

    fun register(nama:String,
                 tempatLahir:String,
                 tanggalLahir: Date,
                 golDarah:String,
                 jenisKelamin:String,
                 pekerjaan:String,
                 alamat:String,
                 noTelepon:String,
                 email:String,
                 password:String,
                 kodeReferral:String
        ){
        val liveData = userRepository.register(
            nama,
            tempatLahir,
            tanggalLahir,
            golDarah,
            jenisKelamin,
            pekerjaan,
            alamat,
            noTelepon,
            email,
            password,
            kodeReferral

        )
        _register.addSource(liveData){ result ->
            _register.value = result
        }
    }

}
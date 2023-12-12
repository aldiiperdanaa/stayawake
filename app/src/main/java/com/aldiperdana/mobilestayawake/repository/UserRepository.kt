package com.aldiperdana.mobilestayawake.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.aldiperdana.mobilestayawake.data.ApiService
import com.aldiperdana.mobilestayawake.helper.ResultState
import com.aldiperdana.mobilestayawake.model.UserModel
import com.aldiperdana.mobilestayawake.response.LoginResponse
import com.aldiperdana.mobilestayawake.response.RegisterResponse
import com.aldiperdana.preference.UserPreference
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException


class UserRepository private constructor(private val apiService: ApiService, private val userPreference: UserPreference){

    private suspend fun saveSession(userModel: UserModel)= userPreference.saveSession(userModel)
    fun login(email : String, password : String): LiveData<ResultState<LoginResponse>> =
        liveData(Dispatchers.IO) {
            emit(ResultState.Loading)
            try {
                val successResponse = apiService.login(email, password)
                val tokenUser = successResponse.loginResult.token
                saveSession(UserModel(email, tokenUser))
                emit(ResultState.Success(successResponse))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
                emit(ResultState.Error(errorResponse.message))
            }
        }


    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ) : UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService,userPreference)
            }.also { instance = it }
    }
}
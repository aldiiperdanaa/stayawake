package com.aldiperdana.mobilestayawake.injection

import android.content.Context
import com.aldiperdana.mobilestayawake.data.ApiConfig
import com.aldiperdana.mobilestayawake.repository.UserRepository
import com.aldiperdana.preference.UserPreference
import com.aldiperdana.preference.dataStore

object Injection {

    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService,pref)
    }

}
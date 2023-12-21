package com.aldiperdana.mobilestayawake.util

import android.content.Context
import com.aldiperdana.mobilestayawake.data.api.ApiConfig
import com.aldiperdana.mobilestayawake.data.repository.UserRepository
import com.aldiperdana.mobilestayawake.preference.UserPreference
import com.aldiperdana.mobilestayawake.preference.dataStore

object Injection {

    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService,pref)
    }

}
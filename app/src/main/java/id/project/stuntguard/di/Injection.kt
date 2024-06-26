package id.project.stuntguard.di

import android.content.Context
import id.project.stuntguard.data.preferences.UserPreferences
import id.project.stuntguard.data.preferences.dataStore
import id.project.stuntguard.data.remote.api.ApiConfig
import id.project.stuntguard.data.repository.Repository

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val mainApiService = ApiConfig.getApiService()
        val emailApiService = ApiConfig.getEmailApiService()
        return Repository.getInstance(pref, mainApiService, emailApiService)
    }
}
package id.project.stuntguard.data.repository

import id.project.stuntguard.data.preferences.UserModel
import id.project.stuntguard.data.preferences.UserPreferences
import id.project.stuntguard.data.remote.api.ApiService
import id.project.stuntguard.data.remote.response.SignInResponse
import id.project.stuntguard.data.remote.response.SignUpResponse
import kotlinx.coroutines.flow.Flow

class Repository private constructor(
    private val preferences: UserPreferences,
    private val apiService: ApiService
) {
    // Preferences :
    suspend fun saveSession(user: UserModel) {
        preferences.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return preferences.getSession()
    }

    suspend fun logout() {
        preferences.logout()
    }

    // Api Service :
    suspend fun signUp(name: String, email: String, password: String): SignUpResponse {
        return apiService.signUp(name = name, email = email, password = password)
    }

    suspend fun signIn(email: String, password: String): SignInResponse {
        return apiService.signIn(email = email, password = password)
    }

    companion object {
        @Volatile
        private var instance: Repository? = null

        fun getInstance(
            preferences: UserPreferences,
            apiService: ApiService
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(preferences, apiService)
            }.also { instance = it }
    }
}
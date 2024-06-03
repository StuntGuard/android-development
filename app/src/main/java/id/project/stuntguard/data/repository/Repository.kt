package id.project.stuntguard.data.repository

import id.project.stuntguard.data.preferences.UserModel
import id.project.stuntguard.data.preferences.UserPreferences
import id.project.stuntguard.data.remote.api.ApiService
import id.project.stuntguard.data.remote.response.GetAllChildResponse
import id.project.stuntguard.data.remote.response.GetPredictHistoryResponse
import id.project.stuntguard.data.remote.response.GetPredictResultResponse
import id.project.stuntguard.data.remote.response.PredictChildResponse
import id.project.stuntguard.data.remote.response.SignInResponse
import id.project.stuntguard.data.remote.response.SignUpResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

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

    suspend fun getAllChild(authToken: String): GetAllChildResponse {
        return apiService.getAllChild(token = authToken)
    }

    suspend fun addNewChild(
        authToken: String,
        name: RequestBody,
        image: MultipartBody.Part,
        gender: RequestBody
    ): SignUpResponse {
        return apiService.addNewChild(
            token = authToken,
            name = name,
            image = image,
            gender = gender
        )
    }

    suspend fun predict(
        authToken: String,
        idChild: Int,
        age: Int,
        gender: String,
        height: Double
    ): PredictChildResponse {
        return apiService.predict(
            token = authToken,
            idChild = idChild,
            age = age,
            gender = gender,
            height = height
        )
    }

    suspend fun getPredictResult(authToken: String, idPredict: Int): GetPredictResultResponse {
        return apiService.getPredictResult(token = authToken, idPredict = idPredict)
    }

    suspend fun getPredictHistory(authToken: String, idChild: Int): GetPredictHistoryResponse {
        return apiService.getPredictHistory(token = authToken, idChild = idChild)
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
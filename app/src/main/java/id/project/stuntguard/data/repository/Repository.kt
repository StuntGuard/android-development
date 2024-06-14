package id.project.stuntguard.data.repository

import id.project.stuntguard.data.model.UserModel
import id.project.stuntguard.data.preferences.UserPreferences
import id.project.stuntguard.data.remote.api.ApiService
import id.project.stuntguard.data.remote.api.EmailApiService
import id.project.stuntguard.data.remote.response.GetAllChildResponse
import id.project.stuntguard.data.remote.response.GetChildPredictHistoryResponse
import id.project.stuntguard.data.remote.response.GetPredictResultResponse
import id.project.stuntguard.data.remote.response.MissionResponse
import id.project.stuntguard.data.remote.response.PredictChildResponse
import id.project.stuntguard.data.remote.response.SignInResponse
import id.project.stuntguard.data.remote.response.SignUpResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class Repository private constructor(
    private val preferences: UserPreferences,
    private val mainApiService: ApiService,
    private val emailApiService: EmailApiService
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

    // Main Api Service :
    suspend fun signUp(name: String, email: String, password: String): SignUpResponse {
        return mainApiService.signUp(name = name, email = email, password = password)
    }

    suspend fun signIn(email: String, password: String): SignInResponse {
        return mainApiService.signIn(email = email, password = password)
    }

    suspend fun getAllChild(authToken: String): GetAllChildResponse {
        return mainApiService.getAllChild(token = authToken)
    }

    suspend fun deleteChild(authToken: String, idChild: Int): SignUpResponse {
        return mainApiService.deleteChild(token = authToken, idChild = idChild)
    }

    suspend fun addNewChild(
        authToken: String,
        name: RequestBody,
        image: MultipartBody.Part,
        gender: RequestBody
    ): SignUpResponse {
        return mainApiService.addNewChild(
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
        return mainApiService.predict(
            token = authToken,
            idChild = idChild,
            age = age,
            gender = gender,
            height = height
        )
    }

    suspend fun getPredictResult(authToken: String, idPredict: Int): GetPredictResultResponse {
        return mainApiService.getPredictResult(token = authToken, idPredict = idPredict)
    }

    suspend fun getPredictHistory(authToken: String, idChild: Int): GetChildPredictHistoryResponse {
        return mainApiService.getPredictHistory(token = authToken, idChild = idChild)
    }

    suspend fun getMissions(authToken: String, idChild: Int): MissionResponse {
        return mainApiService.getMissions(token = authToken, idChild = idChild)
    }

    suspend fun addMission(
        authToken: String,
        idChild: Int,
        title: String,
        description: String,
    ): SignUpResponse {
        return mainApiService.postMission(
            token = authToken,
            idChild = idChild,
            title = title,
            description = description
        )
    }

    suspend fun deleteMissions(authToken: String, idMission: Int): SignUpResponse {
        return mainApiService.deleteMissions(token = authToken, idMission = idMission)
    }

    // Email Api Service :
    suspend fun newEmailCheck(email: String): SignUpResponse {
        return emailApiService.newEmailCheck(email = email)
    }

    suspend fun verifyNewEmail(token: String): SignUpResponse {
        return emailApiService.verifyNewEmail(token = token)
    }

    suspend fun checkEmail(email: String): SignUpResponse {
        return emailApiService.emailCheck(email = email)
    }

    suspend fun verifyEmail(token: String): SignUpResponse {
        return emailApiService.verifyEmail(token = token)
    }

    suspend fun updatePassword(token: String, password: String): SignUpResponse {
        return emailApiService.updatePassword(token = token, password = password)
    }

    companion object {
        @Volatile
        private var instance: Repository? = null

        fun getInstance(
            preferences: UserPreferences,
            mainApiService: ApiService,
            emailApiService: EmailApiService
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(preferences, mainApiService, emailApiService)
            }.also { instance = it }
    }
}
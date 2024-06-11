package id.project.stuntguard.view.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import id.project.stuntguard.data.model.UserModel
import id.project.stuntguard.data.remote.response.SignInResponse
import id.project.stuntguard.data.remote.response.SignUpResponse
import id.project.stuntguard.data.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val repository: Repository) : ViewModel() {
    private val _signInResponse = MutableLiveData<SignInResponse>()
    val signInResponse: LiveData<SignInResponse> = _signInResponse

    private val _errorResponse = MutableLiveData<String?>()
    val errorResponse: LiveData<String?> = _errorResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // Preferences :
    private fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    // Api Service :
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.signIn(email = email, password = password)
                _signInResponse.value = response
                saveSession(UserModel(response.name, email, response.token, true))

            } catch (e: HttpException) {
                Log.e(TAG, "onFailure: ${e.message()}")
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, SignUpResponse::class.java)
                _errorResponse.value = errorBody.message
            }
            _isLoading.value = false
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}
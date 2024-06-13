package id.project.stuntguard.view.reset

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import id.project.stuntguard.data.remote.response.SignUpResponse
import id.project.stuntguard.data.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ResetViewModel(private val repository: Repository) : ViewModel() {
    private val _checkEmailResponse = MutableLiveData<SignUpResponse>()
    val checkEmailResponse: LiveData<SignUpResponse> = _checkEmailResponse

    private val _verifyResponse = MutableLiveData<SignUpResponse>()
    val verifyResponse: LiveData<SignUpResponse> = _verifyResponse

    private val _resetPasswordResponse = MutableLiveData<SignUpResponse>()
    val resetPasswordResponse: LiveData<SignUpResponse> = _resetPasswordResponse

    private val _errorResponse = MutableLiveData<SignUpResponse>()
    val errorResponse: LiveData<SignUpResponse> = _errorResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun checkEmail(email: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.checkEmail(email = email)
                _checkEmailResponse.value = response

            } catch (e: HttpException) {
                Log.e(TAG, "onFailure: ${e.message()}")
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, SignUpResponse::class.java)
                _errorResponse.value = errorBody
            }
            _isLoading.value = false
        }
    }

    fun verifyEmail(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.verifyEmail(token = token)
                _verifyResponse.value = response

            } catch (e: HttpException) {
                Log.e(TAG, "onFailure: ${e.message()}")
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, SignUpResponse::class.java)
                _errorResponse.value = errorBody
            }
            _isLoading.value = false
        }
    }

    fun updatePassword(token: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.updatePassword(token = token, password = password)
                _resetPasswordResponse.value = response

            } catch (e: HttpException) {
                Log.e(TAG, "onFailure: ${e.message()}")
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, SignUpResponse::class.java)
                _errorResponse.value = errorBody
            }
            _isLoading.value = false
        }
    }

    companion object {
        private const val TAG = "ResetViewModel"
    }
}
package id.project.stuntguard.view.verify

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import id.project.stuntguard.data.remote.response.SignUpResponse
import id.project.stuntguard.data.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class VerifyViewModel(private val repository: Repository) : ViewModel() {
    private val _checkEmailResponse = MutableLiveData<SignUpResponse>()
    val checkEmailResponse: LiveData<SignUpResponse> = _checkEmailResponse

    private val _verifyEmailResponse = MutableLiveData<SignUpResponse>()
    val verifyEmailResponse: LiveData<SignUpResponse> = _verifyEmailResponse

    private val _resetPasswordResponse = MutableLiveData<SignUpResponse>()
    val resetPasswordResponse: LiveData<SignUpResponse> = _resetPasswordResponse

    private val _newEmailCheckResponse = MutableLiveData<SignUpResponse>()
    val newEmailCheckResponse: LiveData<SignUpResponse> = _newEmailCheckResponse

    private val _verifyNewEmailResponse = MutableLiveData<SignUpResponse>()
    val verifyNewEmailResponse: LiveData<SignUpResponse> = _verifyNewEmailResponse

    private val _signUpResponse = MutableLiveData<SignUpResponse>()
    val signUpResponse: LiveData<SignUpResponse> = _signUpResponse

    private val _errorResponse = MutableLiveData<String?>()
    val errorResponse: LiveData<String?> = _errorResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    // Reset Password Section :
    fun checkEmail(email: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.checkEmail(email = email)
                _checkEmailResponse.value = response

            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, SignUpResponse::class.java)
                _errorResponse.value = errorBody.message
            }
            _isLoading.value = false
        }
    }

    fun verifyEmail(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.verifyEmail(token = token)
                _verifyEmailResponse.value = response

            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, SignUpResponse::class.java)
                _errorResponse.value = errorBody.message
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
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, SignUpResponse::class.java)
                _errorResponse.value = errorBody.message
            }
            _isLoading.value = false
        }
    }

    // Sign Up Section :
    fun verifyNewEmail(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.verifyNewEmail(token = token)
                _verifyNewEmailResponse.value = response

            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, SignUpResponse::class.java)
                _errorResponse.value = errorBody.message
            }
            _isLoading.value = false
        }
    }

    fun newEmailCheck(email: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.newEmailCheck(email = email)
                _newEmailCheckResponse.value = response

            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, SignUpResponse::class.java)
                _errorResponse.value = errorBody.message
            }
            _isLoading.value = false
        }
    }

    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.signUp(name = name, email = email, password = password)
                _signUpResponse.value = response

            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, SignUpResponse::class.java)
                _errorResponse.value = errorBody.message
            }
            _isLoading.value = false
        }
    }
}
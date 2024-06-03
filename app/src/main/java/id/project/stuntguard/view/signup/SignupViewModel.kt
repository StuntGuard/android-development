package id.project.stuntguard.view.signup

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

class SignupViewModel(private val repository: Repository) : ViewModel() {
    private val _signUpResponse = MutableLiveData<SignUpResponse>()
    val signUpResponse: LiveData<SignUpResponse> = _signUpResponse

    private val _errorResponse = MutableLiveData<String?>()
    val errorResponse: LiveData<String?> = _errorResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.signUp(name = name, email = email, password = password)
                _signUpResponse.value = response
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
        private const val TAG = "SignupViewModel"
    }
}
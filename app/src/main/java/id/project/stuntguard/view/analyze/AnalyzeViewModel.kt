package id.project.stuntguard.view.analyze

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import id.project.stuntguard.data.remote.response.SignUpResponse
import id.project.stuntguard.data.repository.Repository
import id.project.stuntguard.utils.helper.uriToFile
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class AnalyzeViewModel(private val repository: Repository) : ViewModel() {
    private val _addNewChildResponse = MutableLiveData<SignUpResponse>()
    val addNewChildResponse: LiveData<SignUpResponse> = _addNewChildResponse

    private val _errorResponse = MutableLiveData<String?>()
    val errorResponse: LiveData<String?> = _errorResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    fun addNewChild(context: Context, authToken: String, name: String, image: Uri, gender: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val imageFile = uriToFile(image, context)
                val nameRequest = name.toRequestBody("text/plain".toMediaType())
                val genderRequest = gender.toRequestBody("text/plain".toMediaType())
                val imageRequest = imageFile.asRequestBody("image/jpeg".toMediaType())
                val imageMultipartBody = MultipartBody.Part.createFormData(
                    "image",
                    imageFile.name,
                    imageRequest
                )

                val response = repository.addNewChild(
                    authToken = authToken,
                    name = nameRequest,
                    image = imageMultipartBody,
                    gender = genderRequest
                )
                _addNewChildResponse.value = response

            } catch (e: HttpException) {
                Log.d(TAG, "onFailure Add New Child: ${e.message()}")
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, SignUpResponse::class.java)
                _errorResponse.value = errorBody.message
            }
            _isLoading.value = false
        }
    }

    companion object {
        private const val TAG = "AnalyzeViewModel"
    }
}
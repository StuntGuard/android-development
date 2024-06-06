package id.project.stuntguard.view.analyze

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import id.project.stuntguard.data.model.ChildModel
import id.project.stuntguard.data.remote.response.GetAllChildResponse
import id.project.stuntguard.data.remote.response.GetPredictResultResponse
import id.project.stuntguard.data.remote.response.PredictChildResponse
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

    private val _getAllChildResponse = MutableLiveData<GetAllChildResponse>()
    val getAllChildResponse: LiveData<GetAllChildResponse> = _getAllChildResponse

    private val _predictChildResponse = MutableLiveData<PredictChildResponse>()
    val predictChildResponse: LiveData<PredictChildResponse> = _predictChildResponse

    private val _getPredictResultResponse = MutableLiveData<GetPredictResultResponse>()
    val getPredictResultResponse: LiveData<GetPredictResultResponse> = _getPredictResultResponse

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

    fun getAllChild(authToken: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getAllChild(authToken = authToken)
                _getAllChildResponse.value = response

            } catch (e: HttpException) {
                /*
                    Response will always be success :
                    {
                        "status": "Success"
                        "message": "data fetched"
                        "data": []
                    }
                */
            }
            _isLoading.value = false
        }
    }

    fun predict(authToken: String, childData: ChildModel, age: Int, height: Double) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.predict(
                    authToken = authToken,
                    idChild = childData.id,
                    age = age,
                    gender = childData.gender,
                    height = height
                )
                _predictChildResponse.value = response

            } catch (e: HttpException) {
                Log.d(TAG, "onFailure Predict Child: ${e.message()}")
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, SignUpResponse::class.java)
                _errorResponse.value = errorBody.message
            }
            _isLoading.value = false
        }
    }

    fun getPredictResult(authToken: String, idPredict: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response =
                    repository.getPredictResult(authToken = authToken, idPredict = idPredict)
                _getPredictResultResponse.value = response

            } catch (e: HttpException) {
                // TODO
            }
            _isLoading.value = false
        }
    }

    companion object {
        private const val TAG = "AnalyzeViewModel"
    }
}
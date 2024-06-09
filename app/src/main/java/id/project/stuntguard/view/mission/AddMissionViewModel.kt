package id.project.stuntguard.view.mission

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import id.project.stuntguard.data.remote.response.AddMissionResponse
import id.project.stuntguard.data.remote.response.MissionResponse
import id.project.stuntguard.data.remote.response.ResultState
import id.project.stuntguard.data.remote.response.SignUpResponse
import id.project.stuntguard.data.repository.Repository
import id.project.stuntguard.utils.helper.uriToFile
import id.project.stuntguard.view.analyze.AnalyzeViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class AddMissionViewModel (
    private val repository: Repository
): ViewModel() {
    private val _addMissionResponse = MutableLiveData<SignUpResponse>()
    val addMissionResponse: LiveData<SignUpResponse> = _addMissionResponse

    private val _errorResponse = MutableLiveData<String?>()
    val errorResponse: LiveData<String?> = _errorResponse

    fun postMissions(context: Context, authToken: String, idChild: Int, title: String, description: String) {
        viewModelScope.launch {
            try {
                val titleRequest = title.toRequestBody("text/plain".toMediaType())
                val descriptionRequest = description.toRequestBody("text/plain".toMediaType())

                val response = repository.postMission(
                    authToken = authToken,
                    idChild = idChild,
                    title = title,
                    description = description
                )
                _addMissionResponse.value = response

            } catch (e: HttpException) {
                Log.d(TAG, "onFailure Add New Mission: ${e.message()}")
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, SignUpResponse::class.java)
                _errorResponse.value = errorBody.message
            }
        }
    }

    companion object {
        private const val TAG = "AddMissionViewModel"
    }
}
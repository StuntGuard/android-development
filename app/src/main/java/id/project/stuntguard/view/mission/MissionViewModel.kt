package id.project.stuntguard.view.mission

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import id.project.stuntguard.data.remote.response.GetAllChildResponse
import id.project.stuntguard.data.remote.response.MissionResponse
import id.project.stuntguard.data.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MissionViewModel(private val repository: Repository) : ViewModel() {

    private val _getMissionResponse = MutableLiveData<MissionResponse>()
    val getMissionResponse: LiveData<MissionResponse> = _getMissionResponse

    private val _errorResponse = MutableLiveData<String?>()
    val errorResponse: LiveData<String?> = _errorResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _getAllChildResponse = MutableLiveData<GetAllChildResponse>()
    val getAllChildResponse: LiveData<GetAllChildResponse> = _getAllChildResponse

    fun getMissions(authToken: String?, idChild: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getMissions(authToken = authToken, idChild = idChild)
                _getMissionResponse.value = response

            } catch (e: HttpException) {
                Log.e(MissionViewModel.TAG, "onFailure: ${e.message()}")
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody =
                    Gson().fromJson(jsonInString, MissionResponse::class.java)
                _getMissionResponse.value = errorBody
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteMission(authToken: String, idMission: Int) {
        viewModelScope.launch {
            try {
                val response = repository.deleteMissions(authToken = authToken, idMission = idMission)

                _getMissionResponse.value = repository.getMissions(authToken = authToken, idMission)

            } catch (e: HttpException) {
                /*
                    Response will always be success :
                    {
                        "status": "Success"
                        "message": "Child Deleted"
                    }
                */
            }
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

    companion object {
        private const val TAG = "MissionViewModel"
    }
}

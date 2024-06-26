package id.project.stuntguard.view.mission

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import id.project.stuntguard.data.remote.response.GetAllChildResponse
import id.project.stuntguard.data.remote.response.MissionResponse
import id.project.stuntguard.data.remote.response.SignUpResponse
import id.project.stuntguard.data.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MissionViewModel(private val repository: Repository) : ViewModel() {
    private val _getAllChildResponse = MutableLiveData<GetAllChildResponse>()
    val getAllChildResponse: LiveData<GetAllChildResponse> = _getAllChildResponse

    private val _addMissionResponse = MutableLiveData<SignUpResponse>()
    val addMissionResponse: LiveData<SignUpResponse> = _addMissionResponse

    private val _getMissionResponse = MutableLiveData<MissionResponse>()
    val getMissionResponse: LiveData<MissionResponse> = _getMissionResponse

    private val _errorResponse = MutableLiveData<SignUpResponse>()
    val errorResponse: LiveData<SignUpResponse> = _errorResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

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

    fun addMission(authToken: String, idChild: Int, title: String, description: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.addMission(
                    authToken = authToken,
                    idChild = idChild,
                    title = title,
                    description = description
                )
                _addMissionResponse.value = response

            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody =
                    Gson().fromJson(jsonInString, SignUpResponse::class.java)
                _errorResponse.value = errorBody
            }
            _isLoading.value = false
        }
    }

    fun getMissions(authToken: String, idChild: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getMissions(authToken = authToken, idChild = idChild)
                _getMissionResponse.value = response

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

    fun deleteMission(authToken: String, idMission: Int) {
        viewModelScope.launch {
            try {
                repository.deleteMissions(authToken = authToken, idMission = idMission)
                _getAllChildResponse.value = repository.getAllChild(authToken = authToken)

            } catch (e: HttpException) {
                /*
                    Response will always be success :
                    {
                        "status": "Success"
                        "message": "Mission Deleted"
                    }
                */
            }
        }
    }
}

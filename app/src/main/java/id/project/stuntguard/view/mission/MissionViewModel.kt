package id.project.stuntguard.view.mission

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import id.project.stuntguard.data.remote.response.MissionResponse
import id.project.stuntguard.data.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MissionViewModel(private val repository: Repository) : ViewModel() {

    private val _getMissionResponse = MutableLiveData<MissionResponse>()
    val getMissionResponse: LiveData<MissionResponse> = _getMissionResponse

    fun getMissions(authToken: String?, idChild: Int) {
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
            }
        }
    }

    fun deleteMission(authToken: String, idChild: Int) {
        viewModelScope.launch {
            try {
                val response = repository.deleteMission(authToken = authToken, idChild = idChild)

                // to update List of child after delete operation get executed :
                _getAllChildResponse.value = repository.getAllChild(authToken = authToken)

            } catch (e: HttpException) {
                /*
                    Response will always be success :
                    {
                        "status": "Success"
                        "message": "Child Deleted"
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

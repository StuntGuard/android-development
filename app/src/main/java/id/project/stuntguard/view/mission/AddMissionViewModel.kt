package id.project.stuntguard.view.mission

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import id.project.stuntguard.data.remote.response.AddMissionResponse
import id.project.stuntguard.data.remote.response.MissionResponse
import id.project.stuntguard.data.remote.response.ResultState
import id.project.stuntguard.data.repository.Repository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class AddMissionViewModel (
    private val repository: Repository
): ViewModel() {
    private val _responseResult = MutableLiveData<ResultState<AddMissionResponse>>()
    val responseResult: MutableLiveData<ResultState<AddMissionResponse>> = _responseResult

    fun postMission(title: String, description: String){
        viewModelScope.launch {
            try {
                _responseResult.value = ResultState.Loading
                repository.getSession().collect{
                        user ->
                    val response = repository.postMission(user.token, title, description)
                    _responseResult.value = ResultState.Success(response)
                }
            }catch (e: HttpException){
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, MissionResponse::class.java)
                _responseResult.value = ResultState.Error(errorResponse.message)
            }
        }
    }
}
package id.project.stuntguard.view.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.project.stuntguard.data.remote.response.GetAllChildResponse
import id.project.stuntguard.data.remote.response.GetChildPredictHistoryResponse
import id.project.stuntguard.data.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HistoryViewModel(private val repository: Repository) : ViewModel() {
    private val _getAllChildResponse = MutableLiveData<GetAllChildResponse>()
    val getAllChildResponse: LiveData<GetAllChildResponse> = _getAllChildResponse

    private val _getChildPredictHistoryResponse = MutableLiveData<GetChildPredictHistoryResponse>()
    val getChildPredictHistoryResponse: LiveData<GetChildPredictHistoryResponse> = _getChildPredictHistoryResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getAllChild(authToken: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getAllChild(authToken = authToken)
                _getAllChildResponse.value = response

            } catch (e: HttpException) {
                Log.e(TAG, "onFailure: ${e.message()}")

                // TODO
            }
            _isLoading.value = false
        }
    }

    fun getChildPredictHistory(authToken: String, idChild: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getPredictHistory(authToken = authToken, idChild = idChild)
                _getChildPredictHistoryResponse.value = response

            } catch (e: HttpException) {
                Log.e(TAG, "onFailure: ${e.message()}")

                // TODO
            }
            _isLoading.value = false
        }
    }

    companion object {
        private const val TAG = "HistoryViewModel"
    }
}
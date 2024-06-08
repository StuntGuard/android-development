package id.project.stuntguard.view.mission

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.project.stuntguard.data.model.MissionModel
import id.project.stuntguard.data.remote.response.MissionResponse
import id.project.stuntguard.data.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class MissionViewModel(private val repository: Repository) : ViewModel() {

    private val _getMissionResponse = MutableLiveData<MissionResponse>()
    val getMissionResponse: LiveData<MissionResponse> = _getMissionResponse

    fun getMissions(authToken: String?, missionId: Int) {
        viewModelScope.launch {
            try {
                val missions = getMissionsWithRetry(authToken, missionId)
                _missionList.postValue(missions)
            } catch (e: HttpException) {
                if (e.code() == 503) {
                    showError("Server sedang tidak tersedia. Silakan coba lagi nanti.")
                } else {
                    showError("Terjadi kesalahan: ${e.message}")
                }
            } catch (e: Exception) {
                showError("Terjadi kesalahan: ${e.message}")
            }
        }
    }

    private suspend fun getMissionsWithRetry(authToken: String?, missionId: Int): List<MissionModel> {
        return retry(times = 3, initialDelay = 1000L) {
            withContext(Dispatchers.IO) {
                repository.getMissions(authToken, missionId)
            }
        }
    }

    private fun showError(message: String) {
        // Implementasi menampilkan pesan kesalahan ke UI
        // Misalnya dengan LiveData atau mekanisme lain yang sesuai dengan arsitektur aplikasi Anda
    }

    suspend fun <T> retry(
        times: Int,
        initialDelay: Long,
        maxDelay: Long = initialDelay * 2,
        factor: Double = 2.0,
        block: suspend () -> T
    ): T {
        var currentDelay = initialDelay
        repeat(times - 1) {
            try {
                return block()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
        }
        return block() // last attempt
    }

    companion object {
        private const val TAG = "MissionViewModel"
    }
}

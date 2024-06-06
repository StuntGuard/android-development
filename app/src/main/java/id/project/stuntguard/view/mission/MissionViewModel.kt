package id.project.stuntguard.view.mission

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.project.stuntguard.data.model.MissionModel
import id.project.stuntguard.data.repository.Repository
import kotlinx.coroutines.launch

class MissionViewModel(private val repository: Repository) : ViewModel() {

    private val _missionList = MutableLiveData<List<MissionModel>>()
    val missionList: LiveData<List<MissionModel>> = _missionList

    fun getMissions(authToken: String?, missionId: Int) {
        viewModelScope.launch {
            val missions = repository.getMissions(authToken, missionId)
            _missionList.postValue(missions)
        }
    }

    init {

    }
    companion object {
        private const val TAG = "MissionViewModel"
    }
}

package id.project.stuntguard.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import id.project.stuntguard.data.preferences.UserModel
import id.project.stuntguard.data.repository.Repository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository) : ViewModel() {
    // Preferences :
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}
package id.project.stuntguard.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import id.project.stuntguard.data.preferences.UserModel
import id.project.stuntguard.data.repository.Repository

class MainViewModel(private val repository: Repository) : ViewModel() {
    // Preferences :
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}
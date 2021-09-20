package com.example.a4v4.ui.apps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.a4v4.application.Repo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppsViewModel(private val repo: Repo) : ViewModel() {

    val loading : LiveData<Boolean> =   repo.getLoading()

    // constr and initialization
    init {
        viewModelScope.launch(Dispatchers.IO) {
            repo.fetchApps()
        }
    }

    /*--------------------------------------------------------------------------------------------*/
    // getter function for live data communication
    fun getApps()   =   repo.getApps()
}

    /*--------------------------------------------------------------------------------------------*/

// factory method which is a requirement of architecture
class MyViewModelFactory(private val repository: Repo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
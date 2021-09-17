package com.example.a4v4.ui.calllogs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.a4v4.application.Repo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CallLogsViewModel(private val repo: Repo) : ViewModel() {
    // constr and initialization
    init {
        viewModelScope.launch(Dispatchers.IO) {
            repo.fetchLogs()
        }
    }

    /*--------------------------------------------------------------------------------------------*/
    // getter function for live data communication
    fun getLogs()   =   repo.getLogs()
}

    /*--------------------------------------------------------------------------------------------*/

// factory method which is a requirement of architecture
class MyViewModelFactory(private val repository: Repo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CallLogsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CallLogsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
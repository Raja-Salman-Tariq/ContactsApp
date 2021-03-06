package com.example.a4v4.ui.files

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.a4v4.application.Repo

class FilesViewModel(private val repo: Repo) : ViewModel() {

    /*--------------------------------------------------------------------------------------------*/
    // getter function for live data communication from database
    fun getFiles()   =   repo.getFiles()

}

    /*--------------------------------------------------------------------------------------------*/

// factory method which is a requirement of architecture
class MyViewModelFactory(private val repository: Repo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FilesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
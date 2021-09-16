package com.example.a4v4.ui.files

import androidx.lifecycle.*
import com.example.a4v4.application.Repo
import com.example.a4v4.database.ContactsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FilesViewModel(val repo: Repo) : ViewModel() {

    var data    :   LiveData<List<ContactsModel>> =   repo.getContacts(0)/*.asLiveData()*/


//    init {
//        viewModelScope.launch(Dispatchers.IO) {
//            repo.fetchApps()
//        }
//    }
//
    fun getFiles()   =   repo.getFiles()

}

class MyViewModelFactory(private val repository: Repo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FilesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
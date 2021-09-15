package com.example.a4v4.ui.apps

import androidx.lifecycle.*
import com.example.a4v4.application.Repo
import com.example.a4v4.database.ContactsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppsViewModel(val repo: Repo) : ViewModel() {

    var data    :   LiveData<List<ContactsModel>> =   repo.getContacts(0)/*.asLiveData()*/


    init {
        viewModelScope.launch(Dispatchers.IO) {
            repo.fetchApps()
        }
    }

    fun getApps(type:Short): LiveData<List<ContactsModel>> {
        data    =   repo.getContacts(type)
        return data
    }

}

class MyViewModelFactory(private val repository: Repo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
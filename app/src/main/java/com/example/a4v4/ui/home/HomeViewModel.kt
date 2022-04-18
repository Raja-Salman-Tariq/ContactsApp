package com.example.a4v4.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.a4v4.application.Repo
import com.example.a4v4.database.ContactsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(val repo: Repo) : ViewModel() {

    val loading :   LiveData<Boolean>               =   repo.getLoading()
    var data    :   LiveData<List<ContactsModel>>   =   repo.getContacts(0)

    /*--------------------------------------------------------------------------------------------*/

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repo.fetchContacts()
        }
    }

    /*--------------------------------------------------------------------------------------------*/

    fun getContacts(type:Short): LiveData<List<ContactsModel>> {
        data    =   repo.getContacts(type)
        return data
    }
}

    /*--------------------------------------------------------------------------------------------*/

// factory method which is a requirement of architecture
class MyViewModelFactory(private val repository: Repo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
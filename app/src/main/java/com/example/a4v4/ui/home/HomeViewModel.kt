package com.example.a4v4.ui.home

import androidx.lifecycle.*
import com.example.a4v4.application.Repo
import com.example.a4v4.database.DummyModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(val repo: Repo) : ViewModel() {

    val data    :   LiveData<List<DummyModel>> =   repo.getContacts()/*.asLiveData()*/


    init {
        viewModelScope.launch(Dispatchers.IO) {
            repo.fetchContacts()
        }
    }

}

class MyViewModelFactory(private val repository: Repo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
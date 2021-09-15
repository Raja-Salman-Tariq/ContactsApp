package com.example.a4v4.application

import android.app.Application
import com.example.assignment4.database.MyDB

class MyApp : Application() {
    val database by lazy { MyDB.getDatabase(this) }
    lateinit var repository : Repo

    override fun onTerminate() {
        super.onTerminate()
        FileHandler.closeFos()
    }

    fun initRepo() {
        repository=Repo(this, database.contactsDao())
    }
}
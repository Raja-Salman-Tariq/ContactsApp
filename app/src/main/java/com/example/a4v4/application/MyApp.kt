package com.example.a4v4.application

import android.app.Application
import com.example.assignment4.database.MyDB

class MyApp : Application() {
    val database by lazy { MyDB.getDatabase(this) }
    val repository by lazy { Repo(this, database.contactsDao()) }

    override fun onTerminate() {
        super.onTerminate()
        FileHandler.closeFos()
    }
}
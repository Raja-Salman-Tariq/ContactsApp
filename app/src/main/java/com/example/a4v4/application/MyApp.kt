package com.example.a4v4.application

import android.app.Application
import com.example.a4v4.utils.FileHandler
import com.example.a4v4.database.MyDB

class MyApp : Application() {
    val database by lazy { MyDB.getDatabase(this) }
    lateinit var repository : Repo

    // to close a FileHandler resource when app is closed;
    //  This resource may or may not have an open file stream, needed when writing and sharing
    //  contacts csv.
    override fun onTerminate() {
        super.onTerminate()
        FileHandler.closeFos()
    }

    // Repository cannot be instantiated before permissions are granted because data depends on
    //  permission. Thus we allow the repository to be instantiated at a later time by exposing this
    //  function, so that when permissions are granted, repo can be instantiated.
    fun initRepo() {
        repository=Repo(this, database.contactsDao(), database.myFilesDao())
    }
}
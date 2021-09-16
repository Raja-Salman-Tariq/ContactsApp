package com.example.a4v4.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MyFilesDao {

    @Insert()
    suspend fun insert(file: MyFiles)

    @Query("SELECT * FROM MyFiles ORDER BY timestamp DESC")
    fun getFiles(): LiveData<List<MyFiles>>
}
package com.example.a4v4.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.a4v4.database.DummyModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactsDao {

    @Query("SELECT * FROM DummyModel")
    fun getContacts(): LiveData<List<DummyModel>>

    @Query("SELECT * FROM DummyModel WHERE type = :type")
    fun getContacts(type:Short): LiveData<List<DummyModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(contact: DummyModel)

    @Query("DELETE FROM DummyModel")
    suspend fun deleteAll()
}
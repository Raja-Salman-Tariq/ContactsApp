package com.example.a4v4.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ContactsDao {

    @Query("SELECT * FROM ContactsModel")
    fun getContacts(): LiveData<List<ContactsModel>>

    @Query("SELECT * FROM ContactsModel WHERE type = :type")
    fun getContacts(type:Short): LiveData<List<ContactsModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(contact: ContactsModel)

    @Query("DELETE FROM ContactsModel")
    suspend fun deleteAll()
}
package com.example.a4v4.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import javax.sql.DataSource

@Dao
interface ContactsDao {

    @Query("SELECT * FROM ContactsModel")   //  for paging attempt 1
    fun getPagedContacts(): PagingSource<Int,ContactsModel>

    @Query("SELECT * FROM ContactsModel")
    fun getContacts(): LiveData<List<ContactsModel>>

    @Query("SELECT * FROM ContactsModel WHERE type = :type")
    fun getContacts(type:Short): LiveData<List<ContactsModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: ContactsModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(contacts: List<ContactsModel>)

    @Query("DELETE FROM ContactsModel")
    suspend fun deleteAll()
}
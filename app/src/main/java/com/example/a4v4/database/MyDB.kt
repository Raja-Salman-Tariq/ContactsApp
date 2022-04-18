package com.example.a4v4.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// simple and typical room database class
@Database(entities = [ContactsModel::class, MyFiles::class], version = 1, exportSchema = false)
abstract class MyDB : RoomDatabase() {

    abstract fun contactsDao()  :   ContactsDao
    abstract fun myFilesDao()   :   MyFilesDao

    companion object {
        @Volatile
        private var INSTANCE: MyDB? = null

        fun getDatabase(context: Context): MyDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDB::class.java,
                    "my_db"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
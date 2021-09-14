package com.example.assignment4.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.a4v4.database.ContactsDao
import com.example.a4v4.database.DummyModel

@Database(entities = [DummyModel::class], version = 1, exportSchema = false)
abstract class MyDB : RoomDatabase() {

    abstract fun contactsDao(): ContactsDao

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
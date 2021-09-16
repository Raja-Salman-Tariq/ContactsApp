package com.example.a4v4.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MyFiles(@PrimaryKey val name :   String, val timestamp   :   String)
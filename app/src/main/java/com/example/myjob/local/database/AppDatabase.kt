package com.example.myjob.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myjob.domain.entities.ExchangeRates
import com.example.myjob.local.dao.PostDAO

// We need migration if increase version
@Database(entities = [ExchangeRates::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun postDao() : PostDAO
}
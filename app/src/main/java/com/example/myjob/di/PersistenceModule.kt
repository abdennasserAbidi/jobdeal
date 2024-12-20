package com.example.myjob.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.myjob.local.database.AppDatabase
import com.example.myjob.local.database.SharedPreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module that holds database related classes
 */
@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreference {
        return SharedPreference(context)
    }

    /**
     * Provides [AppDatabase] instance
     */
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room
            .databaseBuilder(context, AppDatabase::class.java, "database")
            .build()
    }

    /**
     * Provides [PostDAO] instance
     */
    @Provides
    @Singleton
    fun providePostDAO(appDatabase: AppDatabase) = appDatabase.postDao()
}
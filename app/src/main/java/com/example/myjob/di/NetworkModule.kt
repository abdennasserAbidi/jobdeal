package com.example.myjob.di

import com.example.myjob.remote.service.NetworkModuleFactory
import com.example.myjob.remote.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Singleton

/**
 * Module that holds Network related classes
 */
@ExperimentalSerializationApi
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideServiceEndPoint(): ApiService = NetworkModuleFactory.makeService()
}
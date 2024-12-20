package com.example.myjob.di

import com.example.myapplication.local.source.LocalDataSource
import com.example.myapplication.local.source.LocalDataSourceImp
import com.example.myjob.remote.source.RemoteDataSource
import com.example.myjob.remote.source.RemoteDataSourceImp
import com.example.myjob.data.Repository
import com.example.myjob.data.RepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Module that holds Repository classes
 */
@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideLocalDataSource(localDataSourceImpl: LocalDataSourceImp): LocalDataSource

    @Binds
    abstract fun provideRemoteDataSource(remoteDataSourceImp: RemoteDataSourceImp): RemoteDataSource

    @Binds
    @ViewModelScoped
    abstract fun provideRepository(repository : RepositoryImp) : Repository

}
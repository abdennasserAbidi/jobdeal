package com.example.myjob.di

import com.example.myjob.data.announcement.AnnouncementRepository
import com.example.myjob.data.announcement.AnnouncementRepositoryImp
import com.example.myjob.data.home.HomeRepository
import com.example.myjob.data.home.HomeRepositoryImp
import com.example.myjob.data.invitation.InvitationRepository
import com.example.myjob.data.invitation.InvitationRepositoryImp
import com.example.myjob.data.notification.NotificationRepository
import com.example.myjob.data.notification.NotificationRepositoryImp
import com.example.myjob.data.profile.ProfileRepository
import com.example.myjob.data.profile.ProfileRepositoryImp
import com.example.myjob.data.search.SearchRepository
import com.example.myjob.data.search.SearchRepositoryImp
import com.example.myjob.data.subscription.SubscriptionRepository
import com.example.myjob.data.subscription.SubscriptionRepositoryImp
import com.example.myjob.local.source.LocalDataSource
import com.example.myjob.local.source.LocalDataSourceImp
import com.example.myjob.remote.source.announcement.AnnouncementDataSource
import com.example.myjob.remote.source.announcement.AnnouncementDataSourceImp
import com.example.myjob.remote.source.home.HomeDataSource
import com.example.myjob.remote.source.home.HomeDataSourceImp
import com.example.myjob.remote.source.invitation.InvitationDataSource
import com.example.myjob.remote.source.invitation.InvitationDataSourceImp
import com.example.myjob.remote.source.notification.NotificationDataSource
import com.example.myjob.remote.source.notification.NotificationDataSourceImp
import com.example.myjob.remote.source.profile.ProfileDataSource
import com.example.myjob.remote.source.profile.ProfileDataSourceImp
import com.example.myjob.remote.source.search.SearchDataSource
import com.example.myjob.remote.source.search.SearchDataSourceImp
import com.example.myjob.remote.source.subscription.SubscriptionDataSource
import com.example.myjob.remote.source.subscription.SubscriptionDataSourceImp
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
    abstract fun provideAnnounceDataSource(remoteDataSourceImp: AnnouncementDataSourceImp): AnnouncementDataSource

    @Binds
    @ViewModelScoped
    abstract fun provideAnnounceRepository(repository: AnnouncementRepositoryImp): AnnouncementRepository

    @Binds
    abstract fun provideHomeDataSource(remoteDataSourceImp: HomeDataSourceImp): HomeDataSource

    @Binds
    @ViewModelScoped
    abstract fun provideHomeRepository(repository: HomeRepositoryImp): HomeRepository

    @Binds
    abstract fun provideInvitationDataSource(remoteDataSourceImp: InvitationDataSourceImp): InvitationDataSource

    @Binds
    @ViewModelScoped
    abstract fun provideInvitationRepository(repository: InvitationRepositoryImp): InvitationRepository

    @Binds
    abstract fun provideProfileDataSource(remoteDataSourceImp: ProfileDataSourceImp): ProfileDataSource

    @Binds
    @ViewModelScoped
    abstract fun provideProfileRepository(repository: ProfileRepositoryImp): ProfileRepository

    @Binds
    abstract fun provideSearchDataSource(remoteDataSourceImp: SearchDataSourceImp): SearchDataSource

    @Binds
    @ViewModelScoped
    abstract fun provideSearchRepository(repository: SearchRepositoryImp): SearchRepository

    @Binds
    abstract fun provideSubscriptionDataSource(remoteDataSourceImp: SubscriptionDataSourceImp): SubscriptionDataSource

    @Binds
    @ViewModelScoped
    abstract fun provideSubscriptionRepository(repository: SubscriptionRepositoryImp): SubscriptionRepository

    @Binds
    abstract fun provideNotificationDataSource(remoteDataSourceImp: NotificationDataSourceImp): NotificationDataSource

    @Binds
    @ViewModelScoped
    abstract fun provideNotificationRepository(repository: NotificationRepositoryImp): NotificationRepository
}
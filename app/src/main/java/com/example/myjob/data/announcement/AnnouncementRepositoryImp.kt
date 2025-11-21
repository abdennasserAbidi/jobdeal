package com.example.myjob.data.announcement

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.myjob.base.GenericSource
import com.example.myjob.base.reources.Resource
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.domain.entities.announcement.AnnouncementModel
import com.example.myjob.domain.entities.announcement.CommentsPost
import com.example.myjob.domain.entities.announcement.LikesPost
import com.example.myjob.domain.response.UserResponse
import com.example.myjob.local.database.SharedPreference
import com.example.myjob.remote.source.announcement.AnnouncementDataSource
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AnnouncementRepositoryImp @Inject constructor(
    private val remoteDataSource: AnnouncementDataSource,
    private val sharedPreference: SharedPreference
) : AnnouncementRepository {

    override suspend fun makeAnnouncement(
        idUserConnected: Int,
        announcementModel: AnnouncementModel
    ): Flow<Resource<UserResponse>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = remoteDataSource.makeAnnouncement(idUserConnected, announcementModel)
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }

    override suspend fun removeLike(
        idAnnounce: Int,
        idConnected: Int
    ): Flow<Resource<UserResponse>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = remoteDataSource.removeLike(idAnnounce, idConnected)
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }

    override suspend fun addLikes(
        idAnnounce: Int,
        likesPost: LikesPost
    ): Flow<Resource<UserResponse>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = remoteDataSource.addLikes(idAnnounce, likesPost)
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }
    override suspend fun addComment(
        idAnnounce: Int,
        commentsPost: CommentsPost
    ): Flow<Resource<UserResponse>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = remoteDataSource.addComment(idAnnounce, commentsPost)
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }

    override suspend fun getCompanyAnnouncements(
        id: Int,
    ): Flow<Resource<PagingData<AnnouncementModel>>> = flow {
        val pager = Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 2),
            pagingSourceFactory = {
                GenericSource { currentPage ->
                    val educations =
                        remoteDataSource.getCompanyAnnouncements(id = id, pageNumber = currentPage)

                    val json = Gson().toJson(educations.content)
                    sharedPreference.putString("jsonAnnouncement", json)

                    educations
                }
            }
        ).flow.cachedIn(CoroutineScope(Dispatchers.IO))

        emitAll(
            pager.map { pagingData ->
                Resource(ResourceState.SUCCESS, pagingData, null)
            }
        )
    }.catch { ex ->
        emit(Resource(ResourceState.ERROR, null, ex.message))
    }

    override suspend fun getAnnouncementsCandidate(): Flow<Resource<PagingData<AnnouncementModel>>> = flow {
        val pager = Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 2),
            pagingSourceFactory = {
                GenericSource { currentPage ->
                    val educations =
                        remoteDataSource.getAnnouncementsCandidate(pageNumber = currentPage)

                    val json = Gson().toJson(educations.content)
                    sharedPreference.putString("jsonCandidateAnnouncement", json)

                    educations
                }
            }
        ).flow.cachedIn(CoroutineScope(Dispatchers.IO))

        emitAll(
            pager.map { pagingData ->
                Resource(ResourceState.SUCCESS, pagingData, null)
            }
        )
    }.catch { ex ->
        emit(Resource(ResourceState.ERROR, null, ex.message))
    }

}
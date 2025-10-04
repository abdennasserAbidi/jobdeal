package com.example.myjob.data.invitation

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.myjob.base.GenericSource
import com.example.myjob.base.reources.Resource
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.common.CoroutineWebSocketClient
import com.example.myjob.common.GlobalEntries
import com.example.myjob.domain.entities.InvitationFilter
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.domain.entities.invitation.InvitationParams
import com.example.myjob.domain.entities.invitation.InvitationUser
import com.example.myjob.domain.response.UserResponse
import com.example.myjob.local.database.SharedPreference
import com.example.myjob.remote.source.invitation.InvitationDataSource
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ua.naiksoftware.stomp.dto.LifecycleEvent
import javax.inject.Inject

class InvitationRepositoryImp @Inject constructor(
    private val remoteDataSource: InvitationDataSource,
    private val sharedPreference: SharedPreference
) : InvitationRepository {

    override suspend fun sendInvitation(invitationParams: InvitationParams): Flow<Resource<UserResponse>> =
        flow {
            try {
                // Get data from RemoteDataSource
                val data = remoteDataSource.sendInvitation(invitationParams)
                // Emit data
                emit(Resource(ResourceState.SUCCESS, data, null))
            } catch (ex: Exception) {
                // Emit error
                emit(Resource(ResourceState.ERROR, null, ex.message))
            }
        }

    override suspend fun finishProcess(invitationParams: InvitationParams): Flow<Resource<InvitationParams>> =
        flow {
            try {

                // Get data from RemoteDataSource
                val data = remoteDataSource.finishProcess(invitationParams)
                // Emit data
                emit(Resource(ResourceState.SUCCESS, data, null))
            } catch (ex: Exception) {
                // Emit error
                emit(Resource(ResourceState.ERROR, null, ex.message))
            }
        }

    override suspend fun acceptRejectInvitation(invitationParams: InvitationParams): Flow<Resource<UserResponse>> =
        flow {
            try {
                // Get data from RemoteDataSource
                val data = remoteDataSource.acceptRejectInvitation(invitationParams)
                // Emit data
                emit(Resource(ResourceState.SUCCESS, data, null))
            } catch (ex: Exception) {
                // Emit error
                emit(Resource(ResourceState.ERROR, null, ex.message))
            }
        }

    override suspend fun getInvitationDetail(id: Int, idInvitation: Int): Flow<Resource<InvitationUser>> =
        flow {
            try {
                // Get data from RemoteDataSource
                val data = remoteDataSource.getInvitationDetail(id, idInvitation)
                // Emit data
                Log.i("gktlengtenjgte", "SUCCESS: $data")

                emit(Resource(ResourceState.SUCCESS, data, null))
            } catch (ex: Exception) {
                // Emit error
                Log.i("gktlengtenjgte", "ERROR: ${ex.message}")

                emit(Resource(ResourceState.ERROR, null, ex.message))
            }
        }

    override suspend fun getFilteredInvitations(invitationFiltered: InvitationFilter): Flow<Resource<PagingData<InvitationModel>>> =
        flow {
            val pager = Pager(
                config = PagingConfig(pageSize = 10, prefetchDistance = 2),
                pagingSourceFactory = {
                    GenericSource { currentPage ->

                        val educations =
                            remoteDataSource.getFilteredInvitations(invitationFiltered, pageNumber = currentPage)

                        val json = Gson().toJson(educations.content)
                        sharedPreference.putString("jsonInvitationsFiltered", json)
                        Log.i("jgnrtjkgjth", "educations: ${educations.content}")

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

    override suspend fun getInvitationsByTag(id: Int): Flow<Resource<PagingData<InvitationModel>>> =
        flow {

            val pager = Pager(
                config = PagingConfig(pageSize = 10, prefetchDistance = 2),
                pagingSourceFactory = {
                    GenericSource { currentPage ->
                        val educations =
                            remoteDataSource.getInvitationsByTag(id = id, pageNumber = currentPage)

                        val json = Gson().toJson(educations.content)
                        sharedPreference.putString("jsonOtherInvitations", json)

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

    override suspend fun getInvitations(id: Int): Flow<Resource<PagingData<InvitationModel>>> =
        flow {

            val pager = Pager(
                config = PagingConfig(pageSize = 10, prefetchDistance = 2),
                pagingSourceFactory = {
                    GenericSource { currentPage ->
                        val educations =
                            remoteDataSource.getInvitations(id = id, pageNumber = currentPage)

                        val json = Gson().toJson(educations.content)
                        sharedPreference.putString("jsonInvitations", json)

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

    override suspend fun getCompanyInvitations(id: Int): Flow<Resource<PagingData<InvitationModel>>> =
        flow {
            val pager = Pager(
                config = PagingConfig(pageSize = 10, prefetchDistance = 2),
                pagingSourceFactory = {
                    GenericSource(
                        paramsWS = id
                    ) { currentPage ->

                        val experiences =
                            remoteDataSource.getCompanyInvitations(
                                id = id,
                                pageNumber = currentPage
                            )

                        val json = Gson().toJson(experiences.content)
                        sharedPreference.putString("jsonInvitation", json)

                        experiences
                    }
                }
            ).flow.cachedIn(CoroutineScope(Dispatchers.Default))

            emitAll(
                pager.map { pagingData ->
                    Resource(ResourceState.SUCCESS, pagingData, null)
                }
            )
        }.catch { ex ->
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }

}
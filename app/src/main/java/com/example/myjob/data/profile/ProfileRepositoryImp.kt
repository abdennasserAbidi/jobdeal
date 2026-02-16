package com.example.myjob.data.profile

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.myjob.base.GenericSource
import com.example.myjob.base.reources.Resource
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.domain.entities.CandidateSkills
import com.example.myjob.domain.entities.Educations
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.ProfessionalStatus
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.response.UserResponse
import com.example.myjob.local.database.SharedPreference
import com.example.myjob.remote.source.profile.ProfileDataSource
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProfileRepositoryImp @Inject constructor(
    private val profileDataSource: ProfileDataSource,
    private val sharedPreference: SharedPreference
) : ProfileRepository {

    override suspend fun saveExperience(experience: Experience): Flow<Resource<UserResponse>> =
        flow {
            try {
                // Get data from RemoteDataSource
                val data = profileDataSource.saveExperience(experience)
                // Emit data
                emit(Resource(ResourceState.SUCCESS, data, null))
            } catch (ex: Exception) {
                // Emit error
                emit(Resource(ResourceState.ERROR, null, ex.message))
            }
        }

    override suspend fun saveEducation(educations: Educations): Flow<Resource<UserResponse>> =
        flow {
            try {
                // Get data from RemoteDataSource
                val data = profileDataSource.saveEducation(educations)
                // Emit data
                emit(Resource(ResourceState.SUCCESS, data, null))
            } catch (ex: Exception) {
                // Emit error
                emit(Resource(ResourceState.ERROR, null, ex.message))
            }
        }

    override suspend fun getAllEducations(id: Int): Flow<Resource<PagingData<Educations>>> = flow {
        val pager = Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 2),
            pagingSourceFactory = {
                GenericSource { currentPage ->
                    val educations =
                        profileDataSource.getAllEducations(id = id, pageNumber = currentPage)

                    val json = Gson().toJson(educations.content)
                    sharedPreference.putString("jsonEducation", json)

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

    override suspend fun getAllExperiences(id: Int): Flow<Resource<PagingData<Experience>>> = flow {
        val pager = Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 2),
            pagingSourceFactory = {
                GenericSource { currentPage ->

                    val experiences =
                        profileDataSource.getAllExperiences(id = id, pageNumber = currentPage)

                    val json = Gson().toJson(experiences.content)
                    sharedPreference.putString("jsonExperience", json)

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

    override suspend fun getAllExp(id: Int): Flow<Resource<List<Experience>>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = profileDataSource.getAllExp(id)
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }

    override suspend fun getAllEduc(id: Int): Flow<Resource<List<Educations>>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = profileDataSource.getAllEduc(id)
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }

    override suspend fun savePersonalInfo(user: User): Flow<Resource<UserResponse>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = profileDataSource.savePersonalInfo(user)
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }

    override suspend fun saveProfessionalInfo(user: ProfessionalStatus): Flow<Resource<UserResponse>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = profileDataSource.saveProfessionalInfo(user)
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }

    override suspend fun updateCandidateSkills(user: CandidateSkills): Flow<Resource<UserResponse>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = profileDataSource.updateCandidateSkills(user)
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }

    override suspend fun updateCandidateCompleted(id: Int): Flow<Resource<UserResponse>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = profileDataSource.updateCandidateCompleted(id)
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }

    override suspend fun saveCompanyInfo(user: User): Flow<Resource<UserResponse>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = profileDataSource.saveCompanyInfo(user)
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }

    override suspend fun updateService(user: User): Flow<Resource<UserResponse>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = profileDataSource.updateService(user)
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }

    override suspend fun removeExperience(
        id: Int,
        experienceId: Int
    ): Flow<Resource<UserResponse>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = profileDataSource.removeExperience(id, experienceId)
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }


    override suspend fun removeEducation(id: Int, educationId: Int): Flow<Resource<UserResponse>> =
        flow {
            try {
                // Get data from RemoteDataSource
                val data = profileDataSource.removeEducation(id, educationId)
                // Emit data
                emit(Resource(ResourceState.SUCCESS, data, null))
            } catch (ex: Exception) {
                // Emit error
                emit(Resource(ResourceState.ERROR, null, ex.message))
            }
        }
}
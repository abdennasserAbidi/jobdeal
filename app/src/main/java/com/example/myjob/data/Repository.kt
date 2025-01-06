package com.example.myjob.data

import androidx.paging.PagingData
import com.example.myjob.base.reources.Resource
import com.example.myjob.domain.entities.Educations
import com.example.myjob.domain.entities.ExchangeRates
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.response.LoginResponse
import com.example.myjob.domain.response.UserResponse
import kotlinx.coroutines.flow.Flow

/**
 * Methods of Repository
 */
interface Repository {

    suspend fun getExchangesRate(): Flow<Resource<ExchangeRates>>
    suspend fun changeBaseExchangesRate(base: String): Flow<Resource<ExchangeRates>>
    suspend fun saveUser(user: User): Flow<Resource<LoginResponse>>
    suspend fun saveExperience(experience: Experience): Flow<Resource<UserResponse>>
    suspend fun getAllExperiences(id: Int): Flow<Resource<PagingData<Experience>>>
    suspend fun forgotPassword(email: String): Flow<Resource<UserResponse>>
    suspend fun resetPassword(token: String, newPassword: String): Flow<Resource<UserResponse>>
    suspend fun authenticate(user: User): Flow<Resource<LoginResponse>>
    suspend fun verifyEmail(email: String): Flow<Resource<LoginResponse>>

    suspend fun saveEducation(educations: Educations): Flow<Resource<UserResponse>>
    suspend fun getAllEducations(id: Int): Flow<Resource<PagingData<Educations>>>

    suspend fun getAllExp(id: Int): Flow<Resource<List<Experience>>>
    suspend fun getAllEduc(id: Int): Flow<Resource<List<Educations>>>
    suspend fun getAllUser(): Flow<Resource<PagingData<User>>>

    suspend fun savePersonalInfo(user: User): Flow<Resource<UserResponse>>
    suspend fun removeExperience(id: Int, experienceId: Int): Flow<Resource<UserResponse>>
    suspend fun removeEducation(id: Int, educationId: Int): Flow<Resource<UserResponse>>
    suspend fun getUser(id: Int): Flow<Resource<User>>
}
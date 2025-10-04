package com.example.myjob.data.subscription

import com.example.myjob.base.reources.Resource
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.response.LoginResponse
import com.example.myjob.domain.response.UserResponse
import com.example.myjob.feature.validateprofile.ValidationProfileStatus
import kotlinx.coroutines.flow.Flow

interface SubscriptionRepository {
    suspend fun saveUser(user: User): Flow<Resource<LoginResponse>>
    suspend fun forgotPassword(email: String): Flow<Resource<UserResponse>>
    suspend fun validateCandidateProfile(validationProfileStatus: ValidationProfileStatus): Flow<Resource<UserResponse>>
    suspend fun statusCandidateValidation(id: Int): Flow<Resource<ValidationProfileStatus>>
    suspend fun statusListCandidateValidation(id: Int): Flow<Resource<List<ValidationProfileStatus>>>
    suspend fun resetPassword(token: String, newPassword: String): Flow<Resource<UserResponse>>
    suspend fun authenticate(user: User): Flow<Resource<LoginResponse>>
    suspend fun verifyEmail(email: String): Flow<Resource<LoginResponse>>

}
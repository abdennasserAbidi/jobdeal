package com.example.myjob.data.profile

import androidx.paging.PagingData
import com.example.myjob.base.reources.Resource
import com.example.myjob.domain.entities.CandidateSkills
import com.example.myjob.domain.entities.Educations
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.ProfessionalStatus
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    suspend fun saveExperience(experience: Experience): Flow<Resource<UserResponse>>
    suspend fun getAllExperiences(id: Int): Flow<Resource<PagingData<Experience>>>
    suspend fun saveEducation(educations: Educations): Flow<Resource<UserResponse>>
    suspend fun getAllEducations(id: Int): Flow<Resource<PagingData<Educations>>>
    suspend fun getAllExp(id: Int): Flow<Resource<List<Experience>>>
    suspend fun getAllEduc(id: Int): Flow<Resource<List<Educations>>>
    suspend fun savePersonalInfo(user: User): Flow<Resource<UserResponse>>
    suspend fun saveProfessionalInfo(user: ProfessionalStatus): Flow<Resource<UserResponse>>
    suspend fun updateCandidateSkills(user: CandidateSkills): Flow<Resource<UserResponse>>
    suspend fun updateCandidateCompleted(id: Int): Flow<Resource<UserResponse>>
    suspend fun saveCompanyInfo(user: User): Flow<Resource<UserResponse>>
    suspend fun updateService(user: User): Flow<Resource<UserResponse>>
    suspend fun removeExperience(id: Int, experienceId: Int): Flow<Resource<UserResponse>>
    suspend fun removeEducation(id: Int, educationId: Int): Flow<Resource<UserResponse>>

}
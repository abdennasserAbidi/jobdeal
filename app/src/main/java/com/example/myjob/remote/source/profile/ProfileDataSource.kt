package com.example.myjob.remote.source.profile

import com.example.myjob.base.GenericResponse
import com.example.myjob.domain.entities.CandidateSkills
import com.example.myjob.domain.entities.Educations
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.ProfessionalStatus
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.response.UserResponse

interface ProfileDataSource {
    suspend fun saveExperience(experience: Experience): UserResponse
    suspend fun saveEducation(educations: Educations): UserResponse
    suspend fun getAllExperiences(id: Int, pageNumber: Int): GenericResponse<Experience>
    suspend fun getAllExp(id: Int): List<Experience>
    suspend fun getAllEduc(id: Int): List<Educations>
    suspend fun getAllEducations(id: Int, pageNumber: Int): GenericResponse<Educations>
    suspend fun savePersonalInfo(user: User): UserResponse
    suspend fun updateCandidateSkills(user: CandidateSkills): UserResponse
    suspend fun updateCandidateCompleted(id: Int): UserResponse
    suspend fun saveProfessionalInfo(user: ProfessionalStatus): UserResponse
    suspend fun saveCompanyInfo(user: User): UserResponse
    suspend fun updateService(user: User): UserResponse
    suspend fun removeExperience(id: Int, experienceId: Int): UserResponse
    suspend fun removeEducation(id: Int, educationId: Int): UserResponse
}
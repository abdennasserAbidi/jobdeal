package com.example.myjob.remote.source.profile

import com.example.myjob.base.GenericResponse
import com.example.myjob.domain.entities.CandidateSkills
import com.example.myjob.domain.entities.Educations
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.ProfessionalStatus
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.response.UserResponse
import com.example.myjob.remote.api.ApiService
import javax.inject.Inject

class ProfileDataSourceImp @Inject constructor(
    private val apiService: ApiService
) : ProfileDataSource {
    override suspend fun saveExperience(experience: Experience): UserResponse =
        apiService.saveExperience(experience)

    override suspend fun saveEducation(educations: Educations): UserResponse =
        apiService.saveEducation(educations)

    override suspend fun getAllExperiences(id: Int, pageNumber: Int): GenericResponse<Experience> =
        apiService.getAllExperiences(id, pageNumber)

    override suspend fun getAllExp(id: Int): List<Experience> = apiService.getAllExp(id)
    override suspend fun getAllEduc(id: Int): List<Educations> = apiService.getAllEduc(id)
    override suspend fun getAllEducations(id: Int, pageNumber: Int): GenericResponse<Educations> =
        apiService.getAllEducations(id, pageNumber)

    override suspend fun savePersonalInfo(user: User): UserResponse =
        apiService.savePersonalInfo(user)

    override suspend fun saveProfessionalInfo(user: ProfessionalStatus): UserResponse =
        apiService.updateCandidateProfessional(user)

    override suspend fun updateCandidateSkills(user: CandidateSkills): UserResponse =
        apiService.updateCandidateSkills(user)

    override suspend fun updateCandidateCompleted(id: Int): UserResponse =
        apiService.updateCandidateCompleted(id)

    override suspend fun saveCompanyInfo(user: User): UserResponse =
        apiService.saveCompanyInfo(user)
    override suspend fun updateService(user: User): UserResponse =
        apiService.updateService(user)

    override suspend fun removeExperience(id: Int, experienceId: Int): UserResponse =
        apiService.removeExperience(id, experienceId)

    override suspend fun removeEducation(id: Int, educationId: Int): UserResponse =
        apiService.removeEducation(id, educationId)
}
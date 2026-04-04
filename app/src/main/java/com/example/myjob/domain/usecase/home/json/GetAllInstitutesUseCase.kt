package com.example.myjob.domain.usecase.home.json

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCaseOut
import com.example.myjob.data.home.HomeRepository
import com.example.myjob.domain.entities.json.InstituteModel
import com.example.myjob.domain.qualifiers.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAllInstitutesUseCase @Inject constructor(
    private val repository: HomeRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCaseOut<List<InstituteModel>>() {

    override suspend fun buildRequest(): Flow<Resource<List<InstituteModel>>> {
        return repository.getAllInstitutes().flowOn(dispatcher)
    }
}
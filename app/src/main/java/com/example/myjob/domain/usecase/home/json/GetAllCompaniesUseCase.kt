package com.example.myjob.domain.usecase.home.json

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCaseOut
import com.example.myjob.data.home.HomeRepository
import com.example.myjob.domain.entities.json.CompanyModel
import com.example.myjob.domain.qualifiers.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAllCompaniesUseCase @Inject constructor(
    private val repository: HomeRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCaseOut<List<CompanyModel>>() {

    override suspend fun buildRequest(): Flow<Resource<List<CompanyModel>>> {
        return repository.getAllCompaniesList().flowOn(dispatcher)
    }
}
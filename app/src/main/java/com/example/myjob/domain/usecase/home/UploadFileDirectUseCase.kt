package com.example.myjob.domain.usecase.home

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.home.HomeRepository
import com.example.myjob.domain.qualifiers.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import javax.inject.Inject

class UploadFileDirectUseCase @Inject constructor(
    private val repository: HomeRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<String, Triple<Int, Int, List<MultipartBody.Part>>>() {

    override suspend fun buildRequest(params: Triple<Int, Int, List<MultipartBody.Part>>?): Flow<Resource<String>> {

        return repository.uploadDirect(
            params?.first ?: -1,
            params?.second ?: -1,
            params?.third ?: emptyList()
        ).flowOn(dispatcher)
    }
}
package com.example.myjob.domain.usecase

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.Repository
import com.example.myjob.domain.qualifiers.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class UploadCVUseCase @Inject constructor(
    private val repository: Repository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<String, MultipartBody.Part>() {

    override suspend fun buildRequest(params: MultipartBody.Part?): Flow<Resource<String>> {
        val emptyFile = File("default_name.pdf")  // This can be any dummy file
        val emptyRequestBody: RequestBody = RequestBody.create("application/pdf".toMediaTypeOrNull(), emptyFile)
        val defaultValue = MultipartBody.Part.createFormData("file", "default_name.pdf", emptyRequestBody)

        return repository.uploadFile(params ?: defaultValue).flowOn(dispatcher)
    }
}
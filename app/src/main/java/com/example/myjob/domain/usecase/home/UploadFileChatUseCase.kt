package com.example.myjob.domain.usecase.home

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.home.HomeRepository
import com.example.myjob.domain.qualifiers.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class UploadFileChatUseCase @Inject constructor(
    private val repository: HomeRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<String, Triple<Int, Int, MultipartBody.Part>>() {

    override suspend fun buildRequest(params: Triple<Int, Int, MultipartBody.Part>?): Flow<Resource<String>> {
        val emptyFile = File("default_name.pdf")  // This can be any dummy file
        val emptyRequestBody: RequestBody =
            RequestBody.create("application/pdf".toMediaTypeOrNull(), emptyFile)
        val defaultValue =
            MultipartBody.Part.createFormData("file", "default_name.pdf", emptyRequestBody)

        return repository.uploadChat(
            params?.first ?: -1,
            params?.second ?: -1,
            params?.third ?: defaultValue
        ).flowOn(dispatcher)
    }
}
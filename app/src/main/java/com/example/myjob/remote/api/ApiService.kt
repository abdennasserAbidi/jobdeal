package com.example.myjob.remote.api

import com.example.myjob.common.network.ApiResult
import com.example.myjob.domain.entities.Educations
import com.example.myjob.domain.entities.ExchangeRates
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.User
import com.example.myjob.base.GenericResponse
import com.example.myjob.domain.response.LoginResponse
import com.example.myjob.domain.response.UserResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * The main services that handles all endpoint processes
 */
interface ApiService {

    @GET("latest")
    suspend fun getExchangeRates(): ExchangeRates

    @GET("latest")
    suspend fun changeBaseExchangeRates(@Query("base") base: String): ExchangeRates

    @POST("auth/signup")
    suspend fun saveUser(@Body user: User): ApiResult<LoginResponse>

    @POST("auth/login")
    suspend fun authenticate(@Body user: User): ApiResult<LoginResponse>

    @POST("auth/verification")
    suspend fun verifyEmail(@Query("email") email: String): LoginResponse

    @FormUrlEncoded
    @POST("/auth/forgot-password")
    suspend fun forgotPassword(@Field("email") email: String): UserResponse

    @FormUrlEncoded
    @POST("/auth/reset-password")
    suspend fun resetPassword(
        @Field("token") token: String,
        @Field("newPassword") newPassword: String
    ): UserResponse

    @POST("auth/add")
    suspend fun saveExperience(@Body experience: Experience): UserResponse

    @GET("auth/getAllExperience")
    suspend fun getAllExperiences(
        @Query("id") id: Int,
        @Query("page") pageNumber: Int,
        @Query("size") size: Int = 10
    ): GenericResponse<Experience>

    @POST("auth/addEducation")
    suspend fun saveEducation(@Body educations: Educations): UserResponse

    @GET("auth/getAllEducation")
    suspend fun getAllEducations(
        @Query("id") id: Int,
        @Query("page") pageNumber: Int,
        @Query("size") size: Int = 10
    ): GenericResponse<Educations>

    @GET("auth/getAllJobs")
    suspend fun getAllUser(
        @Query("page") pageNumber: Int,
        @Query("size") size: Int = 10
    ): GenericResponse<User>

    @POST("auth/updateuser")
    suspend fun savePersonalInfo(@Body user: User): UserResponse

    @POST("auth/removeExperience")
    suspend fun removeExperience(
        @Query("id") id: Int,
        @Query("experienceId") experienceId: Int
    ): UserResponse

    @POST("auth/removeEducation")
    suspend fun removeEducation(
        @Query("id") id: Int,
        @Query("educationId") educationId: Int
    ): UserResponse

    @GET("auth/getUser")
    suspend fun getUser(@Query("id") id: Int): User
}
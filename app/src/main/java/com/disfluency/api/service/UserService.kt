package com.disfluency.api.service

import com.disfluency.api.dto.*
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

sealed interface UserService {

    @POST("/login")
    suspend fun login(@Body user: UserDTO): LoginDTO

    @POST("/refreshToken")
    suspend fun loginByToken(@Body refreshToken: RefreshTokenDTO): LoginDTO

    @POST("/signup")
    suspend fun therapistSignUp(@Body newUser: NewTherapistUserDTO): LoginDTO

    @GET("/pending/patient/{patientId}")
    suspend fun pendingPatientById(@Path("patientId") patientId: String): PendingPatientDTO

    @POST("pending/patient/{patientId}/confirm")
    suspend fun confirmPendingPatient(@Path("patientId") patientId: String, @Body patientConfirmation: PatientConfirmationDTO)
}
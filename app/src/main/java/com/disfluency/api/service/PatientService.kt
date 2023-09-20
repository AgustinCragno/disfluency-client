package com.disfluency.api.service

import com.disfluency.api.dto.PatientDTO
import com.disfluency.api.dto.PracticeDTO
import com.disfluency.api.dto.SessionDTO
import com.disfluency.api.dto.SessionPreSignedDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

sealed interface PatientService {

    @GET("/therapists/{therapistId}/patients")
    suspend fun getPatientsByTherapistId(@Path("therapistId") therapistId: String): List<PatientDTO>

    @POST("/therapists/{therapistId}/patients")
    suspend fun createPatientOfTherapist(@Body patient: PatientDTO, @Path("therapistId") therapistId: String): PatientDTO

    @GET("patient/{patientId}/presignedUrl")
    suspend fun getSessionPreSignedUrl(@Path("patientId") patientId: String): SessionPreSignedDTO

    @POST("patient/{patientId}/sessions")
    suspend fun createSession(@Path("patientId") patientId: String, @Body url: PracticeDTO): SessionDTO

    @GET("patient/{patientId}/sessions")
    suspend fun getSessionsByPatientId(@Path("patientId") patientId: String): List<SessionDTO>
}
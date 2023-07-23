package com.disfluency.api.service

import com.disfluency.api.dto.PatientDTO
import com.disfluency.model.Patient
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

sealed interface PatientService {

    @GET("/therapists/{therapistId}/patients")
    suspend fun getPatientsByTherapistId(@Path("therapistId") therapistId: String): List<PatientDTO>

    @POST("/therapists/{therapistId}/patients")
    suspend fun createPatientOfTherapist(@Body patient: PatientDTO, @Path("therapistId") therapistId: String): PatientDTO
}
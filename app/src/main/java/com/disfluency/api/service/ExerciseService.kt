package com.disfluency.api.service

import com.disfluency.api.dto.ExerciseAssignmentDTO
import retrofit2.http.GET
import retrofit2.http.Path

sealed interface ExerciseService{

    @GET("patient/{patientId}/exerciseAssignments")
    suspend fun getAssignmentsByPatientId(@Path("patientId") patientId: String): List<ExerciseAssignmentDTO>
}
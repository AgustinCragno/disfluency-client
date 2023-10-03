package com.disfluency.api.service

import com.disfluency.api.dto.*
import com.disfluency.model.form.FormAssignment
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

sealed interface FormService {

    @GET("patient/{patientId}/formAssignments")
    suspend fun getAssignmentsByPatientId(@Path("patientId") patientId: String): List<FormAssignmentDTO>

    @POST("formAssignments/{formAssignmentId}/formCompletionEntries")
    suspend fun createPracticeInAssignment(@Path("formAssignmentId") formAssignmentId: String, @Body responses: FormEntryDTO): FormAssignmentDTO

    @POST("therapists/{therapistId}/forms")
    suspend fun createFormOfTherapist(@Path("therapistId") therapistId: String, @Body newFormDTO: NewFormDTO): FormDTO

    @POST("formAssignments")
    suspend fun assignFormsToPatients(@Body assignFormsDTO: AssignFormsDTO)
}
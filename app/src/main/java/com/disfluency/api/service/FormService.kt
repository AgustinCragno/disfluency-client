package com.disfluency.api.service

import com.disfluency.api.dto.FormEntryDTO
import com.disfluency.model.form.FormAssignment
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

sealed interface FormService {

    @GET("patient/{patientId}/formAssignments")
    suspend fun getAssignmentsByPatientId(@Path("patientId") patientId: String): List<FormAssignment>

    @POST("formAssignments/{formAssignmentId}/formCompletionEntries")
    suspend fun createPracticeInAssignment(@Path("formAssignmentId") formAssignmentId: String, @Body responses: FormEntryDTO)
}
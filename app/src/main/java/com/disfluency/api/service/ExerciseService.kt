package com.disfluency.api.service

import com.disfluency.api.dto.ExerciseAssignmentDTO
import com.disfluency.api.dto.PracticeDTO
import com.disfluency.api.interceptor.NOT_SEND_AUTH_HEADER
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

sealed interface ExerciseService{

    @GET("patient/{patientId}/exerciseAssignments")
    suspend fun getAssignmentsByPatientId(@Path("patientId") patientId: String): List<ExerciseAssignmentDTO>

    @POST("exercisesAssignments/{exerciseAssignmentId}/practices")
    suspend fun createPracticeInAssignment(@Path("exerciseAssignmentId") exerciseAssignmentId: String): PracticeDTO

    @Headers("Content-Type: audio/mpeg", "$NOT_SEND_AUTH_HEADER: true")
    @PUT
    suspend fun uploadAudioToS3(@Url awsPreSignedUrl: String, @Body file: RequestBody): ResponseBody
}
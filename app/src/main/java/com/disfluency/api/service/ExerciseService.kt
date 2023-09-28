package com.disfluency.api.service

import com.disfluency.api.dto.*
import com.disfluency.api.interceptor.NOT_SEND_AUTH_HEADER
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

sealed interface ExerciseService{

    @GET("patient/{patientId}/exerciseAssignments")
    suspend fun getAssignmentsByPatientId(@Path("patientId") patientId: String): List<ExerciseAssignmentDTO>

    @POST("exercisesAssignments/{exerciseAssignmentId}/practices")
    suspend fun createPracticeInAssignment(@Path("exerciseAssignmentId") exerciseAssignmentId: String): PracticeDTO

    @GET("therapist/{therapistId}/exercises/presigned")
    suspend fun getExerciseSamplePreSignedUrl(@Path("therapistId") therapistId: String): PreSignedUrlDTO

    @POST("therapist/{therapistId}/exercises")
    suspend fun createExerciseOfTherapist(@Body newExercise: NewExerciseDTO, @Path("therapistId") therapistId: String): ExerciseDTO
}
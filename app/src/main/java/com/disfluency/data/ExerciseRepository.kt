package com.disfluency.data

import android.util.Log
import com.disfluency.api.DisfluencyAPI
import com.disfluency.api.dto.AssignExercisesDTO
import com.disfluency.api.dto.NewExerciseDTO
import com.disfluency.api.error.AssignExerciseException
import com.disfluency.api.error.ExerciseCreationException
import com.disfluency.api.error.PatientNotFoundException
import com.disfluency.model.exercise.Exercise
import com.disfluency.model.exercise.ExerciseAssignment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.File

class ExerciseRepository {

    suspend fun getAssignmentsByPatientId(patientId: String): List<ExerciseAssignment> {
        Log.i("exercises", "Retrieving assignments of patient: $patientId")
        try {
            val dtoList = DisfluencyAPI.exerciseService.getAssignmentsByPatientId(patientId)
            Log.i("exercises", "Successfully retrieved ${dtoList.size} exercise assignments of patient: $patientId")
            return dtoList.map { it.asAssignment() }
        }
        catch (e: HttpException){
            throw PatientNotFoundException(patientId)
        }
    }

    suspend fun saveExercisePractice(assignmentId: String, audio: File): ResponseBody {
        val url = DisfluencyAPI.exerciseService.createPracticeInAssignment(assignmentId).recordingUrl
        Log . i ("S3--------", url)
        return DisfluencyAPI.audioUploadString.uploadAudioToS3(
            url,
            audio.asRequestBody("audio/mpeg".toMediaTypeOrNull())
        )
    }

    suspend fun getExerciseSampleUrl(therapistId: String): String {
        Log.i("exercises", "Retrieving pre-signed url for new exercise sample")
        return DisfluencyAPI.exerciseService.getExerciseSamplePreSignedUrl(therapistId).recordingUrl
    }

    suspend fun createExercise(therapistId: String, newExercise: NewExerciseDTO): Exercise {
        Log.i("exercises", "Creating new exercise of therapist: $therapistId")
        try {
            val dto = DisfluencyAPI.exerciseService.createExerciseOfTherapist(newExercise, therapistId)
            Log.i("exercises", "Successfully created exercise ${dto.title} for therapist: $therapistId")
            return dto.asExercise()
        }
        catch (e: HttpException){
            Log.i("exercises", "An error occurred creating exercise ${newExercise.title}: $e")
            throw ExerciseCreationException(newExercise)
        }
    }

    suspend fun assignExercisesToPatients(exerciseIds: List<String>, patientIds: List<String>) {
        Log.i("exercises", "Assigning exercises $exerciseIds to patients $patientIds")
        try {
            val dto = AssignExercisesDTO(patientIds = patientIds, exerciseIds = exerciseIds)
            DisfluencyAPI.exerciseService.assignExercisesToPatients(dto)
            Log.i("exercises", "Successfully assigned exercises $exerciseIds to patients $patientIds")
        }
        catch (e: HttpException){
            Log.i("exercises", "An error occurred assigning exercises $exerciseIds to patients $patientIds")
            throw AssignExerciseException(exerciseIds, patientIds)
        }
    }
}
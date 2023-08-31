package com.disfluency.data

import android.util.Log
import com.disfluency.api.DisfluencyAPI
import com.disfluency.api.error.PatientNotFoundException
import com.disfluency.model.ExerciseAssignment
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
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
        return DisfluencyAPI.exerciseService.uploadAudioToS3(
            url,
            audio.asRequestBody("audio/mpeg".toMediaTypeOrNull())
        )
    }
}
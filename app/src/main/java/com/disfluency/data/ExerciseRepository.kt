package com.disfluency.data

import android.util.Log
import com.disfluency.api.DisfluencyAPI
import com.disfluency.api.error.PatientNotFoundException
import com.disfluency.model.ExerciseAssignment
import retrofit2.HttpException

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
}
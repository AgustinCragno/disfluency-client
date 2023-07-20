package com.disfluency.data

import android.util.Log
import com.disfluency.api.DisfluencyAPI
import com.disfluency.api.error.TherapistNotFoundException
import com.disfluency.model.Patient
import retrofit2.HttpException

class PatientRepository {

    suspend fun getPatientsByTherapist(therapistId: String): List<Patient>{
        Log.i("patients", "Retrieving patients of therapist: $therapistId")
        try {
            val dtoList = DisfluencyAPI.patientService.getPatientsByTherapistId(therapistId)
            Log.i("patients", "Successfully retrieved ${dtoList.size} patients of therapist: $therapistId")
            return dtoList.map { it.asPatient() }
        } catch (e: HttpException){
            throw TherapistNotFoundException(therapistId)
        }
    }
}
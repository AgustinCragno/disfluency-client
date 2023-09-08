package com.disfluency.data

import android.util.Log
import com.disfluency.api.DisfluencyAPI
import com.disfluency.api.dto.PatientDTO
import com.disfluency.api.error.PatientCreationException
import com.disfluency.api.error.TherapistNotFoundException
import com.disfluency.model.user.Patient
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

    suspend fun createPatientForTherapist(therapistId: String, patient: Patient): Patient {
        Log.i("patients", "Creating patient: $patient")
        try {
            val dto = PatientDTO.fromPatient(patient)
            val patientResponse = DisfluencyAPI.patientService.createPatientOfTherapist(dto, therapistId)
            Log.i("patients", "Successfully created patient: $patientResponse of therapist: $therapistId")
            return patientResponse.asPatient()
        } catch (e: HttpException){
            Log.i("patients", "Error creating patient: $patient ==> $e")
            throw PatientCreationException(patient)
        }
    }
}
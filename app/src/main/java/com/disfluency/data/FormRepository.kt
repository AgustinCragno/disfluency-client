package com.disfluency.data

import android.util.Log
import com.disfluency.api.DisfluencyAPI
import com.disfluency.api.dto.FormEntryDTO
import com.disfluency.api.dto.NewFormDTO
import com.disfluency.api.error.FormCreationException
import com.disfluency.api.error.FormEntryCreationException
import com.disfluency.model.form.*
import retrofit2.HttpException

class FormRepository {
    suspend fun getAssignmentsByPatientId(patientId: String): List<FormAssignment>? {
        //TODO: atrapar errores ya agregar logs
        return DisfluencyAPI.formService.getAssignmentsByPatientId(patientId).map { it.asAssignment() }
    }

    suspend fun createFormEntry(formAssignmentId: String, responses: FormEntryDTO): FormAssignment {
        Log.i("forms", "Creating new form entry for assignment: $formAssignmentId")
        try {
            val assignmentDTO = DisfluencyAPI.formService.createPracticeInAssignment(formAssignmentId, responses)
            Log.i("forms", "Successfully submitted entry for assignment: $formAssignmentId")
            return assignmentDTO.asAssignment()
        }
        catch (e: HttpException){
            Log.i("forms", "Error creating entry for assignment: $formAssignmentId ==> $e")
            throw FormEntryCreationException(formAssignmentId)
        }
    }

    suspend fun createFormOfTherapist(therapistId: String, newFormDto: NewFormDTO): Form {
        Log.i("forms", "Creating form for therapist: $therapistId")
        try {
            val formDto = DisfluencyAPI.formService.createFormOfTherapist(therapistId, newFormDto)
            Log.i("forms", "Successfully created form '${newFormDto.title}' for therapist: $therapistId")
            return formDto.asForm()
        }
        catch (e: HttpException){
            Log.i("forms", "Error creating form '${newFormDto.title}' for therapist: $therapistId ==> $e")
            throw FormCreationException(newFormDto)
        }
    }
}
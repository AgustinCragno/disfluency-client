package com.disfluency.data

import android.util.Log
import com.disfluency.api.DisfluencyAPI
import com.disfluency.api.dto.FormEntryDTO
import com.disfluency.model.form.*
import java.time.LocalDate

class FormRepository {
    suspend fun getAssignmentsByPatientId(patientId: String): List<FormAssignment>? {
//        val question = FormQuestion("3", "Todo en orden?", "Seguro?", "No", "Si")
//        val form = Form("2", "Ejemplo de cuestionario", listOf(question))
//        val response = FormQuestionResponse("5", question, 4, "Seee")
//        val entries = FormCompletionEntry("4", LocalDate.now(), listOf(response))
//
//        val question2 = FormQuestion("6", "Como estuvo la fiesta?", "Posta?", "Mala", "Muy buena")
//        val form2 = Form("7", "Actividad social", listOf(question, question2))
//        val response2 = FormQuestionResponse("8", question2, 3, "No..")
//        val entries2 = FormCompletionEntry("9", LocalDate.now(), listOf(response2))
//
//        return listOf(FormAssignment("1", form, LocalDate.now(), listOf(entries, entries2)),
//            FormAssignment("10", form2, LocalDate.now(), listOf(entries2)))
        return DisfluencyAPI.formService.getAssignmentsByPatientId(patientId).map { it.asAssignment() }
    }

    suspend fun createFormEntry(formAssignmentId: String, responses: FormEntryDTO) {
        Log.i("forms", "Creating new form entry")
        DisfluencyAPI.formService.createPracticeInAssignment(formAssignmentId, responses)
    }
}
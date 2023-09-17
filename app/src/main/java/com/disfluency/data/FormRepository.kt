package com.disfluency.data

import com.disfluency.api.dto.FormEntryDTO
import com.disfluency.model.form.*
import java.time.LocalDate

class FormRepository {
    fun getAssignmentsByPatientId(patientId: String): List<FormAssignment>? {
        val question = FormQuestion("3", "Todo en orden?", "Seguro?")
        val form = Form("2", "Ejemplo de cuestionario", listOf(question))
        val response = FormQuestionResponse("5", question, 4, "Seee")
        val entries = FormCompletionEntry("4", LocalDate.now(), listOf(response))

        val question2 = FormQuestion("6", "Como estuvo la fiesta?", "Posta?")
        val form2 = Form("7", "Actividad social", listOf(question, question2))
        val response2 = FormQuestionResponse("8", question2, 3, "No..")
        val entries2 = FormCompletionEntry("9", LocalDate.now(), listOf(response2))

        return listOf(FormAssignment("1", form, LocalDate.now(), listOf(entries, entries2)),
            FormAssignment("10", form2, LocalDate.now(), listOf(entries2)))
    }

    fun createFormEntry(responses: FormEntryDTO) {
        println(responses)
    }
}
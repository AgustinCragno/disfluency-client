package com.disfluency.model.form

import java.time.LocalDate

class FormCompletionEntry(val id: String, val date: LocalDate, val responses: List<FormQuestionResponse>) {
}
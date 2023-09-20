package com.disfluency.model.form

import java.time.LocalDate

class FormAssignment(val id: String, val form: Form, val date: LocalDate, val completionEntries: List<FormCompletionEntry>) {
}
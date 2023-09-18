package com.disfluency.api.dto

import com.disfluency.model.form.FormAssignment
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import java.time.LocalDate

data class FormAssignmentDTO(
    @JsonProperty("id") val id: String,
    @JsonProperty("form") val form: FormDTO,
    @JsonProperty("date") @JsonDeserialize(using = LocalDateDeserializer::class) @JsonSerialize(using = LocalDateSerializer::class) val date: LocalDate,
    @JsonProperty("completionEntries") val completionEntries: List<FormCompletionEntryDTO>
) {
    fun asAssignment(): FormAssignment {
        return FormAssignment(id, form.asForm(), date, completionEntries.map { it.asEntry() })
    }
}
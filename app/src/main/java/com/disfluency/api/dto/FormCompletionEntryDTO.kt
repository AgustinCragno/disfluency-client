package com.disfluency.api.dto

import com.disfluency.model.form.FormCompletionEntry
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import java.time.LocalDate

class FormCompletionEntryDTO(
    @JsonProperty("id") val id: String,
    @JsonProperty("date") @JsonDeserialize(using = LocalDateDeserializer::class) @JsonSerialize(using = LocalDateSerializer::class) val date: LocalDate,
    @JsonProperty("responses") val responses: List<FormQuestionResponseDTO>)
{
    fun asEntry(): FormCompletionEntry{
        return FormCompletionEntry(id, date, responses.map { it.asResponse() })
    }
}
package com.disfluency.api.dto

import com.disfluency.model.ExercisePractice
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import java.time.LocalDateTime

data class ExercisePracticeDTO(
    @JsonProperty("id") val id: String,
    @JsonProperty("date") @JsonDeserialize(using = LocalDateTimeDeserializer::class) @JsonSerialize(using = LocalDateTimeSerializer::class) val date: LocalDateTime,
    @JsonProperty("recordingUrl") val recordingUrl: String
) {
    fun asPractice(): ExercisePractice {
        return ExercisePractice(id, date, recordingUrl)
    }
}
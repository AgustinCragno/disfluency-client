package com.disfluency.api.dto

import com.disfluency.model.ExercisePractice
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class ExercisePracticeDTO(
    @JsonProperty("id") val id: String,
    @JsonProperty("date") @JsonDeserialize(using = LocalDateDeserializer::class) @JsonSerialize(using = LocalDateSerializer::class) val date: LocalDate,
    @JsonProperty("recordingUrl") val recordingUrl: String
) {
    fun asPractice(): ExercisePractice {
        //TODO: cambiar desde el back para que le llegue un localDateTime, y actualizar esta clase
        return ExercisePractice(id, LocalDateTime.of(date, LocalTime.now()), recordingUrl)
    }
}
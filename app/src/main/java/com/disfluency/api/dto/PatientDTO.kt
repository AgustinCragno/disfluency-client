package com.disfluency.api.dto

import com.disfluency.api.serialization.DayOfWeekDeserializer
import com.disfluency.model.Patient
import com.disfluency.model.UserRole
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

@JsonIgnoreProperties(ignoreUnknown = true) //TODO: temporal hasta implementar ejercicios
data class PatientDTO(
    @JsonProperty("id") val id: String,
    @JsonProperty("name") val name: String,
    @JsonProperty("lastName") val lastName: String,
    @JsonProperty("dateOfBirth") @JsonDeserialize(using = LocalDateDeserializer::class) val dateOfBirth: LocalDate,
    @JsonProperty("email") val email: String,
    @JsonProperty("joinedSince") @JsonDeserialize(using = LocalDateDeserializer::class) val joinedSince: LocalDate,
    @JsonProperty("profilePictureUrl") val profilePictureUrl: Int,
    @JsonProperty("weeklyTurn") @JsonDeserialize(using = DayOfWeekDeserializer::class) val weeklyTurn: List<DayOfWeek>,
    @JsonProperty("weeklyHour") @JsonDeserialize(using = LocalTimeDeserializer::class) val weeklyHour: LocalTime
) {

    fun asPatient(): Patient {
        return Patient(id, name, lastName, dateOfBirth, email, joinedSince, profilePictureUrl, weeklyTurn, weeklyHour)
    }
}
package com.disfluency.api.dto

import com.disfluency.api.serialization.DayOfWeekDeserializer
import com.disfluency.api.serialization.DayOfWeekSerializer
import com.disfluency.model.user.Patient
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class PatientSimpleDTO(
    @JsonProperty("id") val id: String,
    @JsonProperty("name") val name: String,
    @JsonProperty("lastName") val lastName: String,
    @JsonProperty("dateOfBirth") @JsonDeserialize(using = LocalDateDeserializer::class) @JsonSerialize(using = LocalDateSerializer::class) val dateOfBirth: LocalDate,
    @JsonProperty("email") val email: String,
    @JsonProperty("joinedSince") @JsonDeserialize(using = LocalDateDeserializer::class) @JsonSerialize(using = LocalDateSerializer::class) val joinedSince: LocalDate,
    @JsonProperty("profilePictureUrl") val profilePictureUrl: Int,
    @JsonProperty("weeklyTurn") @JsonDeserialize(using = DayOfWeekDeserializer::class) @JsonSerialize(using = DayOfWeekSerializer::class) val weeklyTurn: List<DayOfWeek>,
    @JsonProperty("weeklyHour") @JsonDeserialize(using = LocalTimeDeserializer::class) @JsonSerialize(using = LocalTimeSerializer::class) val weeklyHour: LocalTime
) {

    fun asPatient(): Patient {
        return Patient(id, name, lastName, dateOfBirth, email, joinedSince, profilePictureUrl, weeklyTurn, weeklyHour)
    }

    companion object {
        fun fromPatient(patient: Patient): PatientSimpleDTO {
            return PatientSimpleDTO(patient.id, patient.name, patient.lastName, patient.dateOfBirth, patient.email, patient.joinedSince, patient.avatarIndex, patient.weeklyTurn, patient.weeklyHour)
        }
    }
}
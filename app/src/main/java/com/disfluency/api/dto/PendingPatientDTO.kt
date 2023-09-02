package com.disfluency.api.dto

import com.disfluency.model.user.Patient
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate
import java.time.LocalTime

data class PendingPatientDTO(
    @JsonProperty("name") val name: String,
    @JsonProperty("lastName") val lastName: String,
    @JsonProperty("email") val email: String,
    @JsonProperty("profilePictureUrl") val profilePictureUrl: Int
) {
    fun asPatient(id: String): Patient {
        return Patient(id, name, lastName, LocalDate.now(), email, LocalDate.now(), profilePictureUrl, listOf(), LocalTime.now())
    }
}
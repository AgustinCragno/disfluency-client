package com.disfluency.api.dto

import com.disfluency.model.UserRole
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = PatientDTO::class, name = "patient"),
    JsonSubTypes.Type(value = TherapistDTO::class, name = "therapist")
)
interface RoleDTO {
    fun toRole(): UserRole
}
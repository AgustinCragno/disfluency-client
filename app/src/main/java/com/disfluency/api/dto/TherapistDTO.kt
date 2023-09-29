package com.disfluency.api.dto

import com.disfluency.model.user.Therapist
import com.disfluency.model.user.UserRole
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls

@JsonIgnoreProperties(ignoreUnknown = true)
class TherapistDTO(
    @JsonProperty("id") val id: String,
    @JsonProperty("name") val name: String,
    @JsonProperty("lastName") val lastName: String,
    @JsonProperty("profilePictureUrl") val profilePictureUrl: Int,
    @JsonSetter(nulls = Nulls.AS_EMPTY) @JsonProperty("exercises") val exercises: List<ExerciseDTO> = emptyList(),
    @JsonSetter(nulls = Nulls.AS_EMPTY) @JsonProperty("forms") val forms: List<FormDTO> = emptyList()
) : RoleDTO {

    override fun toRole(): UserRole {
        return Therapist(id, exercises.map { it.asExercise() }.toMutableList(), forms.map { it.asForm() })
    }
}
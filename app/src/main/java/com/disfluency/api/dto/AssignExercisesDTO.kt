package com.disfluency.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class AssignExercisesDTO(
    @JsonProperty("patientIds") val patientIds: List<String>,
    @JsonProperty("exerciseIds") val exerciseIds: List<String>
)
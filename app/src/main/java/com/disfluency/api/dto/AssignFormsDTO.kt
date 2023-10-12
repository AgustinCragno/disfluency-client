package com.disfluency.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class AssignFormsDTO(
    @JsonProperty("patientIds") val patientIds: List<String>,
    @JsonProperty("formIds") val formIds: List<String>
)
package com.disfluency.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class NewFormQuestionDTO(
    @JsonProperty("scaleQuestion") val scaleQuestion: String,
    @JsonProperty("followUpQuestion") val followUpQuestion: String,
    @JsonProperty("majorScale") val majorScale: String,
    @JsonProperty("minorScale") val minorScale: String
) {

}

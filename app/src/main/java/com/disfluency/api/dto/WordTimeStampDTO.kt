package com.disfluency.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class WordTimeStampDTO(
    @JsonProperty("start") val start: Float,
    @JsonProperty("end") val end: Float
) {
}
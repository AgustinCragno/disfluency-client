package com.disfluency.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class SessionDTO(
    @JsonProperty("id") val id: String,
    @JsonProperty("analysis") val analysis: AnalysisDTO,
) {}

data class UpdatedAnalysisDTO(
    @JsonProperty("updatedAnalysis") val updatedAnalysis: List<AnalysedWordOutputDTO>
)

data class AnalysedWordOutputDTO(
    @JsonProperty("text") val text: String,
    @JsonProperty("timestamp") val timestamp: WordTimeStampDTO,
    @JsonProperty("disfluency") val disfluency: List<String>
)
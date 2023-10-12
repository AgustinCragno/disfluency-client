package com.disfluency.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class SessionDTO(
    @JsonProperty("id") val id: String,
    @JsonProperty("analysis") val analysis: AnalysisDTO,
) {}

data class UpdatedAnalysisDTO(
    @JsonProperty("updatedAnalysis") val updatedAnalysis: List<AnalysedWordDTO>
)
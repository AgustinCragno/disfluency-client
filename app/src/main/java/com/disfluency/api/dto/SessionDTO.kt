package com.disfluency.api.dto

import com.disfluency.model.analysis.Analysis
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import java.time.LocalDate

data class SessionDTO(
    @JsonProperty("id") val id: String,
    @JsonProperty("analysis") val analysis: AnalysisDTO,
) {}
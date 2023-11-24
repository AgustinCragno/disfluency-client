package com.disfluency.api.dto

import com.disfluency.model.analysis.Analysis
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import java.time.LocalDate

data class AnalysisDTO(
    @JsonProperty("id") val id: String,
    @JsonProperty("date") @JsonDeserialize(using = LocalDateDeserializer::class) @JsonSerialize(using = LocalDateSerializer::class) val date: LocalDate,
    @JsonProperty("recordingUrl") val recordingUrl: String,
    @JsonProperty("transcription") val transcription: List<AnalysedWordDTO>
) {
    fun asAnalysis(): Analysis {
        val analyzedWords = transcription.map { it.asAnalysedWord() }
        return Analysis(id, recordingUrl, date, analyzedWords.toMutableList())
    }
}
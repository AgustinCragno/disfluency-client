package com.disfluency.api.dto

import com.disfluency.model.analysis.AnalysedWord
import com.disfluency.model.analysis.DisfluencyType
import com.fasterxml.jackson.annotation.JsonProperty

data class AnalysedWordDTO(
    @JsonProperty("text") val text: String,
    @JsonProperty("timestamp") val timestamp: WordTimeStampDTO,
    @JsonProperty("disfluency") val disfluencyList: List<DisfluencyType>
) {
    fun asAnalysedWord(): AnalysedWord {
        //TODO: cabiarlo a float
        return AnalysedWord(text, timestamp.start.toInt(), timestamp.end.toInt(), disfluencyList)
    }
}
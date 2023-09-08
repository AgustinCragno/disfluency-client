package com.disfluency.api.dto

import com.disfluency.model.analysis.AnalysisResults
import com.fasterxml.jackson.annotation.JsonProperty

data class AnalysisResultsDTO(
    @JsonProperty("fluencyIndex") val fluencyIndex: Float,
    @JsonProperty("totalWords") val totalWords: Int,
    @JsonProperty("cleanWords") val cleanWords: Int,
    @JsonProperty("totalDisfluencies") val totalDisfluencies: Int,
    @JsonProperty("totalPhrases") val totalPhrases: Int,
    @JsonProperty("averageDisfluenciesPerPhrase") val averageDisfluenciesPerPhrase: Float,
    @JsonProperty("disfluencyResults") val disfluencyResults: List<DisfluencyTypeStatsDTO>
) {
    fun asAnalysisResults(): AnalysisResults {
        return AnalysisResults(
            totalWords = totalWords,
            totalDisfluencies = totalDisfluencies,
            totalPhrases = totalPhrases,
            disfluencyStats = disfluencyResults.map { it.asDisfluencyTypeStats() },
            cleanWordsCount = cleanWords,
            fluencyIndex = fluencyIndex,
            avgDisfluenciesPerPhrase = averageDisfluenciesPerPhrase
        )
    }
}
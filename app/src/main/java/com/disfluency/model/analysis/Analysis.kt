package com.disfluency.model.analysis

import com.disfluency.api.dto.UpdatedAnalysisDTO
import java.time.LocalDate

data class Analysis(
    val id: String,
    val audioUrl: String,
    val date: LocalDate,
    val analysedWords: MutableList<AnalysedWord>? = null,
    val results: AnalysisResults? = null
) {
    fun asUpdatedAnalysisDTO(): UpdatedAnalysisDTO {
        return UpdatedAnalysisDTO(analysedWords.orEmpty().map { it.asAnalysedWordOutputDTO() })
    }
}
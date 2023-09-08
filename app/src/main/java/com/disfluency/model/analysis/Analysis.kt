package com.disfluency.model.analysis

import java.time.LocalDate

data class Analysis(
    val id: String,
    val audioUrl: String,
    val date: LocalDate,
    val analysedWords: List<AnalysedWord>? = null,
    val results: AnalysisResults? = null
)
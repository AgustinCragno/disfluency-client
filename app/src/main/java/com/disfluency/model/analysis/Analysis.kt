package com.disfluency.model.analysis

data class Analysis(
    val id: String,
    val audioUrl: String,
    val analysedWords: List<AnalysedWord>,
    val results: AnalysisResults? = null
)
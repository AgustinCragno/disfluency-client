package com.disfluency.model.analysis

data class AnalysisResults(
    val totalWords: Int,
    val totalDisfluencies: Int,
    val totalPhrases: Int,
    val disfluencyStats: List<DisfluencyTypeStats>,
    val cleanWordsCount: Int,
    val fluencyIndex: Float,
    val avgDisfluenciesPerPhrase: Float
) {
}
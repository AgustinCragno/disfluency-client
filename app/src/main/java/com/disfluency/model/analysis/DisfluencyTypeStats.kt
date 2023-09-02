package com.disfluency.model.analysis

data class DisfluencyTypeStats(
    val type: DisfluencyType,
    val count: Int,
    val percentageInTotalWords: Float,
    val percentageInTotalDisfluencies: Float
) {
}
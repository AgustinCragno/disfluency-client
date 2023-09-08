package com.disfluency.api.dto

import com.disfluency.model.analysis.DisfluencyType
import com.disfluency.model.analysis.DisfluencyTypeStats
import com.fasterxml.jackson.annotation.JsonProperty

data class DisfluencyTypeStatsDTO(
    @JsonProperty("type") val type: DisfluencyType,
    @JsonProperty("count") val count: Int,
    @JsonProperty("percentageInTotalWords") val percentageInTotalWords: Float,
    @JsonProperty("percentageInDisfluencies") val percentageInDisfluencies: Float
) {
    fun asDisfluencyTypeStats(): DisfluencyTypeStats{
        return DisfluencyTypeStats(type, count, percentageInTotalWords, percentageInDisfluencies)
    }
}
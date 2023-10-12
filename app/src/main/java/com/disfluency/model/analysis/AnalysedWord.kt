package com.disfluency.model.analysis

import com.disfluency.api.dto.AnalysedWordDTO
import com.disfluency.api.dto.AnalysedWordOutputDTO
import com.disfluency.api.dto.WordTimeStampDTO

data class AnalysedWord(val word: String, val startTime: Float, val endTime: Float, var disfluency: List<DisfluencyType>? = null) {

    @OptIn(ExperimentalStdlibApi::class)
    fun isTimeInBetween(time: Float): Boolean {
        return time in startTime..<endTime
    }

    fun hasDisfluency(disfluencyType: DisfluencyType): Boolean {
        return disfluency?.contains(disfluencyType) ?: false
    }

    fun removeDisfluency(aDisfluency: DisfluencyType) {
        disfluency = disfluency?.minus(aDisfluency)
    }

    fun addDisfluency(newDisfluency: DisfluencyType) {
        disfluency = disfluency?.plus(newDisfluency) ?: listOf(newDisfluency)
    }

    fun asAnalysedWordOutputDTO(): AnalysedWordOutputDTO {
        return AnalysedWordOutputDTO(word, WordTimeStampDTO(startTime, endTime), disfluency.orEmpty().map { it.name })
    }
}

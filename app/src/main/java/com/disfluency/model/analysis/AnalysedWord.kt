package com.disfluency.model.analysis

data class AnalysedWord(val word: String, val startTime: Float, val endTime: Float, val disfluency: List<DisfluencyType>? = null) {

    @OptIn(ExperimentalStdlibApi::class)
    fun isTimeInBetween(time: Float): Boolean {
        return time in startTime..<endTime
    }
}

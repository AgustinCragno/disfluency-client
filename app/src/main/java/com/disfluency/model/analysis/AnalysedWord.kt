package com.disfluency.model.analysis

data class AnalysedWord(val word: String, val startTime: Int, val endTime: Int, val disfluency: Disfluency? = null) {

    @OptIn(ExperimentalStdlibApi::class)
    fun isTimeInBetween(time: Int): Boolean {
        return time in startTime..<endTime
    }
}

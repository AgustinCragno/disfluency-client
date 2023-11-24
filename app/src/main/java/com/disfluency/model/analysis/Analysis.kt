package com.disfluency.model.analysis

import com.disfluency.api.dto.UpdatedAnalysisDTO
import java.time.LocalDate

data class Analysis(
    val id: String,
    val audioUrl: String,
    val date: LocalDate,
    val analysedWords: MutableList<AnalysedWord>? = null,
    val results: AnalysisResults? = null,
//    private var originalWords: List<AnalysedWord>? = analysedWords
) {
    fun asUpdatedAnalysisDTO(): UpdatedAnalysisDTO {
//        saveOriginalAnalysis()
        return UpdatedAnalysisDTO(analysedWords.orEmpty().map { it.asAnalysedWordOutputDTO() })
    }
//
//    fun saveOriginalAnalysis() {
//        originalWords = analysedWords?.map {
//            val disfluencies = mutableListOf<DisfluencyType>()
//            it.disfluency?.let { d -> disfluencies.addAll(d) }
//
//            it.copy(
//                word = it.word,
//                startTime = it.startTime,
//                endTime = it.endTime,
//                disfluency = disfluencies
//            )
//        }
//    }
//
//    fun resetToOriginalAnalysis() {
//        originalWords?.let { original ->
//            analysedWords?.let { modified ->
//
//                original.forEachIndexed { i, w ->
//                    modified[i].word = w.word
//                    modified[i].disfluency = w.disfluency
//                }
//            }
//
//            println(analysedWords)
//        }
//    }
}
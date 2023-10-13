package com.disfluency.model.exercise

import com.disfluency.model.analysis.Analysis
import java.time.LocalDateTime

data class ExercisePractice(
    val id: String,
    val date: LocalDateTime,
    val recordingUrl: String,
    val analysis: Analysis? = null
)
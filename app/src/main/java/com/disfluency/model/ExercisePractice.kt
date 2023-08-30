package com.disfluency.model

import java.time.LocalDate
import java.time.LocalDateTime

data class ExercisePractice(
    val id: String,
    val date: LocalDateTime,
    val recordingUrl: String
)
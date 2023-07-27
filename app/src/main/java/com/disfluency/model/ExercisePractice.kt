package com.disfluency.model

import java.time.LocalDate

data class ExercisePractice(
    val id: String,
    val date: LocalDate,
    val recordingUrl: String
)
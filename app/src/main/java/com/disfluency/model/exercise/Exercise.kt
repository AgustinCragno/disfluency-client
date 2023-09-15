package com.disfluency.model.exercise

data class Exercise(
    val id: String,
    val title: String,
    val instruction: String,
    val phrase: String? = null,
    val sampleRecordingUrl: String
) {
}
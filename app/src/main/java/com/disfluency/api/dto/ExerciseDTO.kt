package com.disfluency.api.dto

import com.disfluency.model.exercise.Exercise
import com.fasterxml.jackson.annotation.JsonProperty

data class ExerciseDTO(
    @JsonProperty("id") val id: String,
    @JsonProperty("title") val title: String,
    @JsonProperty("instruction") val instruction: String,
    @JsonProperty("phrase") val phrase: String? = null,
    @JsonProperty("sampleRecordingUrl") val sampleRecordingUrl: String
) {
    fun asExercise(): Exercise {
        return Exercise(id, title, instruction, phrase, sampleRecordingUrl)
    }
}
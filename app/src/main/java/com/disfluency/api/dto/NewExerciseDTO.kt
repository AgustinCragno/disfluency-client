package com.disfluency.api.dto

import com.disfluency.model.exercise.Exercise
import com.fasterxml.jackson.annotation.JsonProperty

data class NewExerciseDTO(
    @JsonProperty("title") val title: String,
    @JsonProperty("instruction") val instruction: String,
    @JsonProperty("phrase") val phrase: String? = null,
    @JsonProperty("sampleRecordingUrl") val sampleRecordingUrl: String
) {
}
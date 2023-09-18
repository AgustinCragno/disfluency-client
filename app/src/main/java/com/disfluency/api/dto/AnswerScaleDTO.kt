package com.disfluency.api.dto

import com.disfluency.model.form.AnswerScale
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class AnswerScaleDTO(
    @JsonProperty("tag") val tag: String,
    @JsonProperty("value") val value: Int
) {
    fun asScale(): AnswerScale {
        return AnswerScale(tag, value)
    }
}
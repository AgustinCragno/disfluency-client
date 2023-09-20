package com.disfluency.api.dto

import com.disfluency.model.form.FormQuestion
import com.fasterxml.jackson.annotation.JsonProperty

data class FormQuestionDTO(
    @JsonProperty("id") val id: String,
    @JsonProperty("scaleQuestion") val scaleQuestion: String,
    @JsonProperty("followUpQuestion") val followUpQuestion: String,
    @JsonProperty("majorScale") val majorScale: AnswerScaleDTO,
    @JsonProperty("minorScale") val minorScale: AnswerScaleDTO
) {
    fun asQuestion(): FormQuestion {
        return FormQuestion(id, scaleQuestion, followUpQuestion, minorScale.tag, majorScale.tag)
    }
}

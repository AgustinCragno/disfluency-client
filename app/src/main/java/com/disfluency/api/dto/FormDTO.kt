package com.disfluency.api.dto

import com.disfluency.model.form.Form
import com.fasterxml.jackson.annotation.JsonProperty

data class FormDTO(
    @JsonProperty("id") val id: String,
    @JsonProperty("title") val title: String,
    @JsonProperty("questions") val questions: List<FormQuestionDTO>
) {
    fun asForm(): Form {
        return Form(id, title, questions.map { it.asQuestion() })
    }
}
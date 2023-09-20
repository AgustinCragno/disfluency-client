package com.disfluency.api.dto

import com.disfluency.model.form.FormQuestionResponse
import com.fasterxml.jackson.annotation.JsonProperty

class FormQuestionResponseDTO(
    @JsonProperty("id") val id: String,
    @JsonProperty("question") val question: FormQuestionDTO,
    @JsonProperty("scaleResponse") val scaleResponse: Int,
    @JsonProperty("followUpResponse") val followUpResponse: String
) {
    fun asResponse(): FormQuestionResponse{
        return FormQuestionResponse(id, question.asQuestion(), scaleResponse, followUpResponse)
    }
}
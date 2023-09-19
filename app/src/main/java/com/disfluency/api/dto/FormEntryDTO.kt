package com.disfluency.api.dto

import com.disfluency.screens.patient.forms.QuestionResponse

data class FormEntryDTO(val responses: List<QuestionResponseDTO>)

data class QuestionResponseDTO(val idQuestion: String, val scaleResponse: Int, val followUpResponse: String){

    constructor(questionResponse: QuestionResponse) : this(questionResponse.idQuestion, questionResponse.scaleResponse.value.toInt(), questionResponse.followUpResponse.value) {

    }
}
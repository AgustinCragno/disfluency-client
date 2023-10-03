package com.disfluency.api.dto

import com.disfluency.screens.therapist.forms.FormQuestionCreation
import com.fasterxml.jackson.annotation.JsonProperty

data class NewFormDTO(
    @JsonProperty("title") val title: String,
    @JsonProperty("questions") val questions: List<NewFormQuestionDTO>
) {
    companion object {
        fun from(title: String, questions: List<FormQuestionCreation>): NewFormDTO {
            return NewFormDTO(
                title = title,
                questions = questions.map {
                    NewFormQuestionDTO(
                        scaleQuestion = it.question.value,
                        followUpQuestion = it.followUp.value,
                        minorScale = it.scaleStart.value,
                        majorScale = it.scaleEnd.value
                    )
                }
            )
        }
    }
}
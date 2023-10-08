package com.disfluency.screens.therapist.forms.burndown

import com.disfluency.model.form.FormCompletionEntry
import com.disfluency.model.form.FormQuestion
import java.time.LocalDate


data class FormQuestionReport(val question: FormQuestion, val scoresByAssignmentDate: Map<LocalDate, Int>)

fun generateResponsesReport(questions: List<FormQuestion>, responses: List<FormCompletionEntry>): List<FormQuestionReport> {
    return questions.map { q ->
        FormQuestionReport(
            question = q,
            scoresByAssignmentDate = responses.associate { entry ->
                Pair(
                    entry.date,
                    entry.responses.find { it.question.id == q.id }!!.scaleResponse
                )
            }
        )
    }
}


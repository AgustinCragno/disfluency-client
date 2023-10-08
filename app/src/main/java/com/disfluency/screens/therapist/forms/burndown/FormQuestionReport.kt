package com.disfluency.screens.therapist.forms.burndown

import androidx.compose.ui.graphics.Color
import com.disfluency.model.form.FormCompletionEntry
import com.disfluency.model.form.FormQuestion
import java.time.LocalDate
import java.util.*


private val COLORS = listOf(
    Color.Blue,
    Color.Magenta,
    Color.Red,
    Color(0xFF29792C),
    Color(0xFF16B6B2)
)


data class FormQuestionReport(
    val question: FormQuestion,
    val scoresByAssignmentDate: Map<LocalDate, Int>,
    val chartColor: Color = COLORS.random()
)

fun generateResponsesReport(questions: List<FormQuestion>, responses: List<FormCompletionEntry>): List<FormQuestionReport> {

    val colorQueue: Queue<Color> = LinkedList()

    val shuffledColors = COLORS.shuffled()
    (questions.indices).forEach {
        colorQueue.add(
            shuffledColors[it % shuffledColors.size]
        )
    }

    return questions.map { q ->
        FormQuestionReport(
            question = q,
            scoresByAssignmentDate = responses.associate { entry ->
                Pair(
                    entry.date,
                    entry.responses.find { it.question.id == q.id }!!.scaleResponse
                )
            },
            chartColor = colorQueue.remove()
        )
    }
}


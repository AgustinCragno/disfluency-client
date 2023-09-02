package com.disfluency.model.exercise

import java.time.LocalDate

data class ExerciseAssignment(
    val id: String,
    val exercise: Exercise,
    val dateOfAssignment: LocalDate,
    val practiceAttempts: MutableList<ExercisePractice>
) {
    fun attemptsCount(): Int {
        return practiceAttempts.size
    }
}
package com.disfluency.model.user

import com.disfluency.api.serialization.WeeklyProgressMapDeserializer
import com.disfluency.model.exercise.ExerciseAssignment
import com.disfluency.model.form.FormAssignment
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import java.time.LocalDate

data class PatientProgressInfo(
    val exercisesCount: Int,
    val formsCount: Int,
    val exercisesCompletedTodayCount: Int,
    val formsCompletedTodayCount: Int,
    val allAssignmentsCompletedToday: Boolean,
    val exercisesCompletedInLastTwoWeeks: Int,
    val lastAssignedExercise: ExerciseAssignment?,
    val lastAssignedForm: FormAssignment?,
    val pendingExercises: List<ExerciseAssignment>,
    val pendingForms: List<FormAssignment>,
    val weeklyProgressMap: Map<LocalDate, Int>
)

    //TODO: recortar los ejercicios y cuestionarios para que manden solo el titulo y fecha y id

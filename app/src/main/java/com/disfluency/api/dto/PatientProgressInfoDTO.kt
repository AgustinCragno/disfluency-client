package com.disfluency.api.dto

import com.disfluency.api.serialization.WeeklyProgressMapDeserializer
import com.disfluency.model.user.PatientProgressInfo
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import java.time.LocalDate

data class PatientProgressInfoDTO(
    @JsonProperty("exercisesCount") val exercisesCount: Int,
    @JsonProperty("formsCount") val formsCount: Int,
    @JsonProperty("exercisesCompletedTodayCount") val exercisesCompletedTodayCount: Int,
    @JsonProperty("formsCompletedTodayCount") val formsCompletedTodayCount: Int,
    @JsonProperty("allAssignmentsCompletedToday") val allAssignmentsCompletedToday: Boolean,
    @JsonProperty("exercisesCompletedInLastTwoWeeks") val exercisesCompletedInLastTwoWeeks: Int,
    @JsonProperty("lastAssignedExercise") val lastAssignedExercise: ExerciseAssignmentDTO?,
    @JsonProperty("lastAssignedForm") val lastAssignedForm: FormAssignmentDTO?,
    @JsonProperty("pendingExercises") val pendingExercises: List<ExerciseAssignmentDTO>,
    @JsonProperty("pendingForms") val pendingForms: List<FormAssignmentDTO>,
    @JsonProperty("weeklyProgressMap") @JsonDeserialize(using = WeeklyProgressMapDeserializer::class) val weeklyProgressMap: Map<LocalDate, Int>
){
    fun asProgress(): PatientProgressInfo {
        return PatientProgressInfo(
            exercisesCount,
            formsCount,
            exercisesCompletedTodayCount,
            formsCompletedTodayCount,
            allAssignmentsCompletedToday,
            exercisesCompletedInLastTwoWeeks,
            lastAssignedExercise?.asAssignment(),
            lastAssignedForm?.asAssignment(),
            pendingExercises.map { it.asAssignment() },
            pendingForms.map { it.asAssignment() },
            weeklyProgressMap
        )
    }
}

    //TODO: recortar los ejercicios y cuestionarios para que manden solo el titulo y fecha y id

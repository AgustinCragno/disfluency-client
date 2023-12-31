package com.disfluency.api.dto

import com.disfluency.model.exercise.ExerciseAssignment
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import java.time.LocalDate

data class ExerciseAssignmentDTO(
    @JsonProperty("id") val id: String,
    @JsonProperty("exercise") val exercise: ExerciseDTO,
    @JsonProperty("dateOfAssignment") @JsonDeserialize(using = LocalDateDeserializer::class) @JsonSerialize(using = LocalDateSerializer::class) val dateOfAssignment: LocalDate,
    @JsonProperty("practiceAttempts") @JsonSetter(nulls = Nulls.AS_EMPTY) val practiceAttempts: MutableList<ExercisePracticeDTO>
){
    fun asAssignment(): ExerciseAssignment {
        return ExerciseAssignment(id, exercise.asExercise(), dateOfAssignment, practiceAttempts.map { it.asPractice() }.toMutableList())
    }
}
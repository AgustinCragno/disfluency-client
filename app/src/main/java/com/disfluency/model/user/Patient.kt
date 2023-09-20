package com.disfluency.model.user

import com.disfluency.model.exercise.ExerciseAssignment
import com.disfluency.model.form.FormAssignment
import com.disfluency.utilities.avatar.AvatarManager
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import kotlin.random.Random

data class Patient(
    val id: String,
    val name: String,
    val lastName: String,
    val dateOfBirth: LocalDate,
    val email: String,
    val joinedSince: LocalDate,
    val avatarIndex: Int,
    val weeklyTurn: List<DayOfWeek>,
    val weeklyHour: LocalTime,
    val exercises: List<ExerciseAssignment> = emptyList(),
    val forms: List<FormAssignment> = emptyList()
) : UserRole {
    fun fullName(): String {
        return "$name $lastName"
    }

    fun fullNameFormal(): String {
        return "$lastName, $name"
    }

    fun avatar(): Int {
        return AvatarManager.getAvatarId(avatarIndex)
    }

    fun getCompletedExercisesCount(): Int {
        return exercises
            .map { e -> e.practiceAttempts }
            .count { e -> e.isNotEmpty() }
    }

    fun getPendingExercisesCount(): Int {
        return exercises
            .map { e -> e.practiceAttempts }
            .count { e -> e.isEmpty() }
    }

    fun getCompletedQuestionnairesCount(): Int {
        //TODO: implement
        return 0
    }

    fun getPendingQuestionnairesCount(): Int {
        //TODO: implement
        return 0
    }

    fun getRecordedSessionsCount(): Int {
        //TODO: implement
        return 0
    }
}
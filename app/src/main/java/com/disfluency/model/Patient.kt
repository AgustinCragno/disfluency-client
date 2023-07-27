package com.disfluency.model

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
    val exercises: List<ExerciseAssignment> = listOf()
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
        return Random.nextInt(5)
    }

    fun getPendingQuestionnairesCount(): Int {
        //TODO: implement
        return Random.nextInt(5)
    }

    fun getRecordedSessionsCount(): Int {
        //TODO: implement
        return Random.nextInt(5)
    }
}
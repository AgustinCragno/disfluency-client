package com.disfluency.model

import com.disfluency.utilities.avatar.AvatarManager
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

data class Patient(
    val id: String,
    val name: String,
    val lastName: String,
    val dateOfBirth: LocalDate,
    val email: String,
    val joinedSince: LocalDate,
    private val avatarIndex: Int,
    val weeklyTurn: List<DayOfWeek>,
    val weeklyHour: LocalTime
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
}
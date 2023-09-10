package com.disfluency.model.user

import com.disfluency.model.exercise.Exercise

data class Therapist(
    val id: String,
    val exercises: List<Exercise> = emptyList()
) : UserRole {
}
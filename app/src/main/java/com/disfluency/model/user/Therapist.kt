package com.disfluency.model.user

import com.disfluency.model.exercise.Exercise
import com.disfluency.model.form.Form

data class Therapist(
    val id: String,
    val exercises: List<Exercise> = emptyList(),
    val forms: List<Form> = emptyList()
) : UserRole {
}
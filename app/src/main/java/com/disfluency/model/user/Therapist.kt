package com.disfluency.model.user

import com.disfluency.model.exercise.Exercise
import com.disfluency.model.form.Form

data class Therapist(
    val id: String,
    val exercises: MutableList<Exercise> = mutableListOf(),
    val forms: MutableList<Form> = mutableListOf()
) : UserRole {

    fun addExercise(exercise: Exercise) {
        exercises.add(exercise)
    }

    fun addForm(form: Form) {
        forms.add(form)
    }
}
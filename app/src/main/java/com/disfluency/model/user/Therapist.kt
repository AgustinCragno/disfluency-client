package com.disfluency.model.user

import com.disfluency.model.exercise.Exercise
import com.disfluency.model.form.Form
import com.disfluency.utilities.avatar.AvatarManager
import java.time.LocalTime

data class Therapist(
    val id: String,
    val name: String,
    val lastName: String,
    val profilePictureUrl: Int,
    val todayPatients: List<Patient> = mutableListOf(),
    val exercises: MutableList<Exercise> = mutableListOf(),
    val forms: MutableList<Form> = mutableListOf()
) : UserRole {

    fun addExercise(exercise: Exercise) {
        exercises.add(exercise)
    }

    fun addForm(form: Form) {
        forms.add(form)
    }

    fun getNextPatients(): List<Patient>{
        return todayPatients.filter { p -> p.weeklyHour > LocalTime.now() }.sortedBy { p -> p.weeklyHour }
    }

    fun avatar(): Int {
        return AvatarManager.getAvatarId(profilePictureUrl)
    }
}

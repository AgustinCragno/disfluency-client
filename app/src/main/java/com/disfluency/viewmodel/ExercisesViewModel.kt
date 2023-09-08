package com.disfluency.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.disfluency.api.error.ExerciseAssignmentNotFoundException
import com.disfluency.data.ExerciseRepository
import com.disfluency.model.exercise.ExerciseAssignment
import kotlinx.coroutines.launch

class ExercisesViewModel : ViewModel() {

    private val exerciseRepository = ExerciseRepository()

    val assignments: MutableState<List<ExerciseAssignment>?> = mutableStateOf(null)

    fun getAssignmentsOfPatient(patientId: String) = viewModelScope.launch {
        assignments.value = exerciseRepository.getAssignmentsByPatientId(patientId)
    }

    fun getAssignmentById(assignmentId: String): ExerciseAssignment {
        return assignments.value?.first { it.id == assignmentId } ?: throw ExerciseAssignmentNotFoundException(assignmentId)
    }
}
package com.disfluency.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.disfluency.api.error.AssignExerciseException
import com.disfluency.data.ExerciseRepository
import com.disfluency.viewmodel.states.ConfirmationState
import kotlinx.coroutines.launch

class AssignmentsViewModel : ViewModel() {

    private val exerciseRepository = ExerciseRepository()

    val assignmentConfirmationState = mutableStateOf(ConfirmationState.DONE)


    fun assignExercisesToPatients(exerciseIds: List<String>, patientIds: List<String>) = viewModelScope.launch {
        assignmentConfirmationState.value = ConfirmationState.LOADING
        try {
            exerciseRepository.assignExercisesToPatients(exerciseIds = exerciseIds, patientIds = patientIds)
            assignmentConfirmationState.value = ConfirmationState.SUCCESS
        } catch (e: AssignExerciseException){
            assignmentConfirmationState.value = ConfirmationState.ERROR
        }
    }
}
package com.disfluency.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.disfluency.api.dto.FormEntryDTO
import com.disfluency.api.error.ExerciseAssignmentNotFoundException
import com.disfluency.api.error.FormAssignmentNotFoundException
import com.disfluency.api.error.FormEntryCreationException
import com.disfluency.data.FormRepository
import com.disfluency.model.form.FormAssignment
import com.disfluency.viewmodel.states.ConfirmationState
import kotlinx.coroutines.launch

class FormsViewModel : ViewModel() {

    private val formsRepository = FormRepository()

    val assignedForms: MutableState<List<FormAssignment>?> = mutableStateOf(null)

    val completionConfirmationState: MutableState<ConfirmationState> = mutableStateOf(ConfirmationState.DONE)

    fun getAssignmentsOfPatient(patientId: String) = viewModelScope.launch {
        assignedForms.value = formsRepository.getAssignmentsByPatientId(patientId)
    }

    fun getAssignmentById(assignmentId: String): FormAssignment {
        return assignedForms.value?.first { it.id == assignmentId } ?: throw FormAssignmentNotFoundException(assignmentId)
    }

    fun completeFormAssignment(formAssignmentId: String, responses: FormEntryDTO) = viewModelScope.launch {
        completionConfirmationState.value = ConfirmationState.LOADING
        try {
            val updatedAssignment = formsRepository.createFormEntry(formAssignmentId, responses)
            updateAssignmentInList(updatedAssignment)
            completionConfirmationState.value = ConfirmationState.SUCCESS
        }
        catch (e: FormEntryCreationException){
            completionConfirmationState.value = ConfirmationState.ERROR
        }
    }

    private fun updateAssignmentInList(updatedAssignment: FormAssignment) {
        assignedForms.value?.let { list ->
            val oldAssignment = list.find { it.id == updatedAssignment.id }
            oldAssignment?.let {
                assignedForms.value = list.minus(oldAssignment).plus(updatedAssignment)
            }
        }
    }
}

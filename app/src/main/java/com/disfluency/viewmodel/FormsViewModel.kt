package com.disfluency.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.disfluency.api.dto.FormEntryDTO
import com.disfluency.api.error.ExerciseAssignmentNotFoundException
import com.disfluency.data.FormRepository
import com.disfluency.model.form.FormAssignment
import kotlinx.coroutines.launch

class FormsViewModel : ViewModel() {

    private val formsRepository = FormRepository()

    val assignedForms: MutableState<List<FormAssignment>?> = mutableStateOf(null)

    fun getAssignmentsOfPatient(patientId: String) = viewModelScope.launch {
        assignedForms.value = formsRepository.getAssignmentsByPatientId(patientId)
    }

    fun getAssignmentById(assignmentId: String): FormAssignment {
        return assignedForms.value?.first { it.id == assignmentId } ?: throw ExerciseAssignmentNotFoundException(assignmentId)
    }

    fun completeFormAssignment(formAssignmentId: String, responses: FormEntryDTO) = viewModelScope.launch {
        formsRepository.createFormEntry(formAssignmentId, responses)
    }
}

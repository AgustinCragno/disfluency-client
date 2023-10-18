package com.disfluency.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.disfluency.api.error.PatientCreationException
import com.disfluency.api.error.PatientNotFoundException
import com.disfluency.data.PatientRepository
import com.disfluency.model.user.Patient
import com.disfluency.viewmodel.states.ConfirmationState
import kotlinx.coroutines.launch

class PatientsViewModel : ViewModel(){

    private val patientRepository = PatientRepository()

    val patients: MutableState<List<Patient>?> = mutableStateOf(null)

    val creationConfirmationState: MutableState<ConfirmationState> = mutableStateOf(ConfirmationState.DONE)

    fun getPatientsByTherapist(therapistId: String) = viewModelScope.launch {
        patients.value = patientRepository.getPatientsByTherapist(therapistId)
        //TODO: por ahi hacer algo con la excepcion?
    }

    fun createPatientForTherapist(therapistId: String, patient: Patient) = viewModelScope.launch {
        creationConfirmationState.value = ConfirmationState.LOADING
        try {
            val newPatient = patientRepository.createPatientForTherapist(therapistId, patient)
            patients.value = patients.value?.plus(newPatient)
            creationConfirmationState.value = ConfirmationState.SUCCESS
        }
        catch (exception: PatientCreationException){
            creationConfirmationState.value = ConfirmationState.ERROR
        }
    }

    fun getPatientById(patientId: String): Patient? {
        return patients.value?.first { it.id == patientId }
    }
}
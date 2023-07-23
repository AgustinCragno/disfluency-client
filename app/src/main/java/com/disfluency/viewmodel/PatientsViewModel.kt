package com.disfluency.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.disfluency.api.dto.PatientDTO
import com.disfluency.data.PatientRepository
import com.disfluency.model.Patient
import kotlinx.coroutines.launch

class PatientsViewModel : ViewModel(){

    private val patientRepository = PatientRepository()

    val patients: MutableState<List<Patient>?> = mutableStateOf(null)

    val newlyCreatedPatient: MutableState<Patient?> = mutableStateOf(null)

    fun getPatientsByTherapist(therapistId: String) = viewModelScope.launch {
        patients.value = patientRepository.getPatientsByTherapist(therapistId)
        //TODO: por ahi hacer algo con la excepcion?
    }

    fun createPatientForTherapist(therapistId: String, patient: Patient) = viewModelScope.launch {
        newlyCreatedPatient.value = patientRepository.createPatientForTherapist(therapistId, patient)
        patients.value = patients.value?.plus(newlyCreatedPatient.value!!)
    }
}
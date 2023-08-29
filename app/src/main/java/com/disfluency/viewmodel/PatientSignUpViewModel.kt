package com.disfluency.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.disfluency.api.error.PatientConfirmationException
import com.disfluency.api.error.PatientNotFoundException
import com.disfluency.api.error.TherapistCreationException
import com.disfluency.data.UserRepository
import com.disfluency.model.Patient
import com.disfluency.viewmodel.states.ConfirmationState
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

class PatientSignUpViewModel : ViewModel() {

    private val userRepository = UserRepository()

    val user = mutableStateOf<Patient?>(null)
    val password = mutableStateOf("")

    val retrievalState = mutableStateOf(ConfirmationState.LOADING)
    val signupState = mutableStateOf(ConfirmationState.DONE)

    fun retrieveUser(token: String) = viewModelScope.launch {
        retrievalState.value = ConfirmationState.LOADING
        try {
            user.value = userRepository.getPendingPatient(token)
            retrievalState.value = ConfirmationState.SUCCESS
        } catch (e: PatientNotFoundException){
            retrievalState.value = ConfirmationState.ERROR
        }
    }

    fun signUp() = viewModelScope.launch {
        signupState.value = ConfirmationState.LOADING
        try {
            userRepository.confirmPatient(user.value!!.id, password.value)
            signupState.value = ConfirmationState.SUCCESS
        }catch (e: PatientConfirmationException){
            signupState.value = ConfirmationState.ERROR
        }
    }
}
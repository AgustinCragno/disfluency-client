package com.disfluency.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.disfluency.model.Patient
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

class PatientSignUpViewModel : ViewModel() {

    val user = mutableStateOf<Patient?>(null)
    val password = mutableStateOf("")

    fun retrieveUser(token: String) = viewModelScope.launch {
        //TODO: pegar a endpoint
        user.value = Patient(
            id = token,
            name = "Agustin",
            lastName = "Cragno",
            dateOfBirth = LocalDate.now(),
            email = "cragno.agustin@gmail.com",
            joinedSince = LocalDate.now(),
            avatarIndex = 3,
            weeklyTurn = listOf(DayOfWeek.MONDAY),
            weeklyHour = LocalTime.now()
        )
    }

    fun signUp() = viewModelScope.launch {
        //TODO: pegar a endpoint de confirmacion de sign-up
    }
}
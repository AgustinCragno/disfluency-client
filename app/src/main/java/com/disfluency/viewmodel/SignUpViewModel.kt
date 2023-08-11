package com.disfluency.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.disfluency.api.error.TherapistCreationException
import com.disfluency.data.UserRepository
import com.disfluency.viewmodel.states.ConfirmationState
import kotlinx.coroutines.launch

class SignUpViewModel(private val loggedUserViewModel: LoggedUserViewModel) : ViewModel() {

    private val userRepository = UserRepository()

    val email = mutableStateOf("")
    val password = mutableStateOf("")

    val avatarIndex = mutableStateOf(1)
    val name = mutableStateOf("")
    val lastName = mutableStateOf("")

    val signupState = mutableStateOf(ConfirmationState.DONE)

    fun signUp() = viewModelScope.launch {
        signupState.value = ConfirmationState.LOADING
        try {
            val createdUser = userRepository.signup(email.value, password.value, name.value, lastName.value, avatarIndex.value)
            signupState.value = ConfirmationState.SUCCESS
            loggedUserViewModel.registerLoggedUser(createdUser)
        }catch (e: TherapistCreationException){
            signupState.value = ConfirmationState.ERROR
        }
    }
}
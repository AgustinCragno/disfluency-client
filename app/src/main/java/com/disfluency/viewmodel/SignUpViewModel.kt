package com.disfluency.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {

    val email = mutableStateOf("")
    val password = mutableStateOf("")

    val avatarIndex = mutableStateOf(1)
    val name = mutableStateOf("")
    val lastName = mutableStateOf("")

    var loginState by mutableStateOf(LoginState.INPUT)
        private set

    fun signUp() = viewModelScope.launch {

    }
}
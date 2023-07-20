package com.disfluency.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.disfluency.api.error.UserNotFoundException
import com.disfluency.data.UserRepository
import com.disfluency.model.User
import kotlinx.coroutines.launch

class LoggedUserViewModel : ViewModel() {

    private val userRepository = UserRepository()

    var firstLogin by mutableStateOf(true)
        private set

    private var user by mutableStateOf<User?>(null)

    var loginState by mutableStateOf(LoginState.INPUT)
        private set

    fun login(account: String, password: String) = viewModelScope.launch {
        loginState = LoginState.SUBMITTED
        try {
            user = userRepository.login(account, password)
            firstLogin = false
            loginState = LoginState.AUTHENTICATED
        }
        catch (exception: UserNotFoundException){
            loginState = LoginState.NOT_FOUND
        }
    }

    fun logout() = viewModelScope.launch {
        user?.let { userRepository.logout(it) }
        user = null
        loginState = LoginState.INPUT
    }


    fun getLoggedUser(): User {
        return user ?: throw IllegalStateException("User has not logged in")
    }
}

enum class LoginState {
    INPUT, NOT_FOUND, SUBMITTED, AUTHENTICATED;
}
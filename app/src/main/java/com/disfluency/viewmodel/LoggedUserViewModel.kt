package com.disfluency.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.disfluency.api.error.ExpiredTokenException
import com.disfluency.api.error.UserNotFoundException
import com.disfluency.data.UserRepository
import com.disfluency.model.User
import com.disfluency.model.UserRole
import com.disfluency.viewmodel.states.LoginState
import kotlinx.coroutines.launch

class LoggedUserViewModel : ViewModel() {

    private val userRepository = UserRepository()

    var firstLoadDone = mutableStateOf(false)

    private var user by mutableStateOf<UserRole?>(null)

    var loginState by mutableStateOf(LoginState.INPUT)
        private set

    fun login(account: String, password: String) = viewModelScope.launch {
        loginState = LoginState.SUBMITTED
        try {
            val user = userRepository.login(account, password)
            registerLoggedUser(user)
        }
        catch (exception: UserNotFoundException){
            loginState = LoginState.NOT_FOUND
        }
    }

    fun login(refreshToken: String) = viewModelScope.launch {
        loginState = LoginState.SUBMITTED
        try {
            val user = userRepository.login(refreshToken)
            registerLoggedUser(user)
        }
        catch (exception: ExpiredTokenException){
            loginState = LoginState.EXPIRED
        }
    }

    fun logout() = viewModelScope.launch {
        user?.let { userRepository.logout() }
        user = null
        loginState = LoginState.INPUT
    }

    fun registerLoggedUser(userRole: UserRole){
        user = userRole
        loginState = LoginState.AUTHENTICATED
    }

    fun getLoggedUser(): UserRole {
        return user ?: throw IllegalStateException("User has not logged in")
    }
}
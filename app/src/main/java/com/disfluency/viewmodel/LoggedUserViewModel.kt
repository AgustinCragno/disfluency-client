package com.disfluency.viewmodel

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.disfluency.data.UserRepository
import com.disfluency.model.User
import kotlinx.coroutines.launch

class LoggedUserViewModel : ViewModel() {

    private val userRepository = UserRepository()

    var firstLogin by mutableStateOf(true)
        private set

    private var user by mutableStateOf<User?>(null)

    val isLoggedIn by derivedStateOf { user != null }

    fun login(account: String, password: String) = viewModelScope.launch {
        user = userRepository.login(account, password)
        firstLogin = false
    }

    fun logout() = viewModelScope.launch {
        user?.let { userRepository.logout(it) }
        user = null
    }


    fun getLoggedUser(): User {
        return user ?: throw IllegalStateException("User has not logged in")
    }
}
package com.disfluency.data

import android.util.Log
import com.disfluency.api.DisfluencyAPI
import com.disfluency.api.dto.UserDTO
import com.disfluency.api.error.UserNotFoundException
import com.disfluency.model.Therapist
import com.disfluency.model.User
import com.disfluency.model.UserRole
import retrofit2.HttpException

class UserRepository {

    suspend fun login(account: String, password: String): User{
        Log.i("login", "User login attempt: $account")
        try {
            val dto = UserDTO(account, password)
            val userRoleDTO = DisfluencyAPI.userService.login(dto)
            Log.i("login", "Successfully logged in as user: $account")
            return User(account, password, userRoleDTO.toRole())

        } catch (e: HttpException){
            throw UserNotFoundException(account)
        }
    }

    suspend fun logout(user: User){
        //TODO: implementation
    }
}
package com.disfluency.data

import android.util.Log
import com.disfluency.api.DisfluencyAPI
import com.disfluency.api.dto.UserDTO
import com.disfluency.api.error.ExpiredTokenException
import com.disfluency.api.error.UserNotFoundException
import com.disfluency.api.session.SessionManager
import com.disfluency.model.Therapist
import com.disfluency.model.User
import com.disfluency.model.UserRole
import retrofit2.HttpException

class UserRepository {

    suspend fun login(account: String, password: String): UserRole {
        Log.i("login", "User login attempt: $account")
        try {
            val dto = UserDTO(account, password)
            val loginDTO = DisfluencyAPI.userService.login(dto)
            Log.i("login", "Successfully logged in as user: $account")
            SessionManager.saveAccessToken(loginDTO.accessToken)
            SessionManager.saveRefreshToken(loginDTO.refreshToken)
            return loginDTO.user.toRole()
        } catch (e: HttpException){
            throw UserNotFoundException(account)
        }
    }

    suspend fun login(refreshToken: String): UserRole {
        Log.i("login", "User login attempt by refresh token: $refreshToken")
        try {
            val loginDTO = DisfluencyAPI.userService.loginByToken(refreshToken)
            Log.i("login", "Successfully logged in by refresh token: $refreshToken")
            SessionManager.saveAccessToken(loginDTO.accessToken)
            SessionManager.saveRefreshToken(loginDTO.refreshToken)
            return loginDTO.user.toRole()
        } catch (e: HttpException){
            throw ExpiredTokenException(refreshToken)
        }
    }

    suspend fun logout(){
        //TODO: implementation
    }
}
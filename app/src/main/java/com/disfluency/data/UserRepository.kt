package com.disfluency.data

import android.util.Log
import com.disfluency.api.DisfluencyAPI
import com.disfluency.api.dto.NewTherapistDTO
import com.disfluency.api.dto.NewTherapistUserDTO
import com.disfluency.api.dto.RefreshTokenDTO
import com.disfluency.api.dto.UserDTO
import com.disfluency.api.error.ExpiredTokenException
import com.disfluency.api.error.TherapistCreationException
import com.disfluency.api.error.UserNotFoundException
import com.disfluency.api.session.SessionManager
import com.disfluency.model.Therapist
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
            val loginDTO = DisfluencyAPI.userService.loginByToken(RefreshTokenDTO(refreshToken))
            Log.i("login", "Successfully logged in by refresh token: $refreshToken")
            SessionManager.saveAccessToken(loginDTO.accessToken)
            SessionManager.saveRefreshToken(loginDTO.refreshToken)
            return loginDTO.user.toRole()
        } catch (e: HttpException){
            Log.i("login", "Could not login by token error: $e")
            throw ExpiredTokenException(refreshToken)
        }
    }

    suspend fun signup(account: String, password: String, name: String, lastName: String, avatarIndex: Int): Therapist {
        Log.i("signup", "Therapist user sign up attempt for: $account")
        try {
            val therapistData = NewTherapistDTO(name, lastName, avatarIndex)
            val userData = NewTherapistUserDTO(account, password, therapistData)
            val loginDTO = DisfluencyAPI.userService.therapistSignUp(userData)
            Log.i("signup", "Successfully created therapist for: $account")
            SessionManager.saveAccessToken(loginDTO.accessToken)
            SessionManager.saveRefreshToken(loginDTO.refreshToken)
            return loginDTO.user.toRole() as Therapist
        } catch (e: HttpException){
            Log.i("signup", "Could not create therapist for $account, error: $e")
            throw TherapistCreationException(account)
        }
    }

    suspend fun logout(){
        //TODO: implementation
    }
}
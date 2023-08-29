package com.disfluency.data

import android.util.Log
import com.disfluency.api.DisfluencyAPI
import com.disfluency.api.dto.*
import com.disfluency.api.error.*
import com.disfluency.api.session.SessionManager
import com.disfluency.model.Patient
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

    suspend fun getPendingPatient(id: String): Patient {
        Log.i("signup", "Retrieving pending patient of id: $id")
        try {
            val pendingPatientDTO = DisfluencyAPI.userService.pendingPatientById(id)
            Log.i("signup", "Successfully retrieved pending patient info")
            return pendingPatientDTO.asPatient(id)
        } catch (e: HttpException){
            Log.i("signup", "Could not retrieve patient, error: $e")
            throw PatientNotFoundException(id)
        }
    }

    suspend fun confirmPatient(id: String, password: String) {
        Log.i("signup", "Sending pending patient confirmation of id: $id")
        try {
            val confirmationDTO = PatientConfirmationDTO(password)
            DisfluencyAPI.userService.confirmPendingPatient(id, confirmationDTO)
            Log.i("signup", "Successfully confirmed pending patient")
        } catch (e: HttpException){
            Log.i("signup", "Could not confirm patient, error: $e")
            throw PatientConfirmationException(id)
        }
    }
}
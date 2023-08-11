package com.disfluency.api.service

import com.disfluency.api.dto.*
import retrofit2.http.Body
import retrofit2.http.POST

sealed interface UserService {

    @POST("/login")
    suspend fun login(@Body user: UserDTO): LoginDTO

    @POST("/refreshToken")
    suspend fun loginByToken(@Body refreshToken: RefreshTokenDTO): LoginDTO

    @POST("/signup")
    suspend fun therapistSignUp(@Body newUser: NewTherapistUserDTO): LoginDTO
}
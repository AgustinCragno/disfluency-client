package com.disfluency.api.service

import com.disfluency.api.dto.LoginDTO
import com.disfluency.api.dto.RoleDTO
import com.disfluency.api.dto.UserDTO
import retrofit2.http.Body
import retrofit2.http.POST

sealed interface UserService {

    @POST("/login")
    suspend fun login(@Body user: UserDTO): LoginDTO

    @POST("/login")
    suspend fun loginByToken(@Body refreshToken: String): LoginDTO
}
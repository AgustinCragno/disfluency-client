package com.disfluency.api

import com.disfluency.api.service.UserService
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object DisfluencyAPI {

    //TODO: read from property file
    private const val API_BASE_URL = "http://192.168.0.20:8081/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
    }

    val userService: UserService by lazy { retrofit.create(UserService::class.java) }
}
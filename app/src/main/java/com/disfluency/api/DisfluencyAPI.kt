package com.disfluency.api

import com.disfluency.api.service.UserService
import com.disfluency.utilities.PropertiesReader
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object DisfluencyAPI {
    private val retrofit: Retrofit by lazy {
        val host = PropertiesReader.getProperty("API_HOST")
        val port = PropertiesReader.getProperty("API_PORT")

        Retrofit.Builder()
            .baseUrl("http://$host:$port/")
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
    }

    val userService: UserService by lazy { retrofit.create(UserService::class.java) }
}
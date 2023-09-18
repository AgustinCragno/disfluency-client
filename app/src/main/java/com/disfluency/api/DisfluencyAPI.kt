package com.disfluency.api

import com.disfluency.api.interceptor.AuthInterceptor
import com.disfluency.api.service.ExerciseService
import com.disfluency.api.service.FormService
import com.disfluency.api.service.PatientService
import com.disfluency.api.service.UserService
import com.disfluency.utilities.PropertiesReader
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object DisfluencyAPI {
    private val retrofit: Retrofit by lazy {
        val host = PropertiesReader.getProperty("API_HOST")
        val port = PropertiesReader.getProperty("API_PORT")

        Retrofit.Builder()
            .baseUrl("http://$host:$port/")
            .addConverterFactory(JacksonConverterFactory.create())
            .client(okhttpClient())
            .build()
    }

    private fun okhttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    val userService: UserService by lazy { retrofit.create(UserService::class.java) }
    val patientService: PatientService by lazy { retrofit.create(PatientService::class.java) }
    val exerciseService: ExerciseService by lazy { retrofit.create(ExerciseService::class.java) }
    val formService: FormService by lazy { retrofit.create(FormService::class.java) }
}
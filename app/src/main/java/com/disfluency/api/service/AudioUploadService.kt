package com.disfluency.api.service

import com.disfluency.api.interceptor.NOT_SEND_AUTH_HEADER
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Url

sealed interface AudioUploadService  {

    @Headers("Content-Type: audio/mpeg", "$NOT_SEND_AUTH_HEADER: true")
    @PUT
    suspend fun uploadAudioToS3(@Url awsPreSignedUrl: String, @Body file: RequestBody): ResponseBody
}
package com.disfluency.api.service

import com.disfluency.api.dto.AnalysisResultsDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface AnalysisService {

    @GET("/session/{sessionId}/result")
    suspend fun getResultsBySessionId(@Path("sessionId") sessionId: String): AnalysisResultsDTO

    @GET("/analysis/{analysisId}/result")
    suspend fun getResultsById(@Path("analysisId") analysisId: String): AnalysisResultsDTO
}
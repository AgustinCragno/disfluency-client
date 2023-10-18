package com.disfluency.api.service

import com.disfluency.api.dto.AnalysisResultsDTO
import com.disfluency.api.dto.UpdatedAnalysisDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface AnalysisService {

    @GET("/session/{sessionId}/result")
    suspend fun getResultsBySessionId(@Path("sessionId") sessionId: String): AnalysisResultsDTO

    @PUT("/session/{sessionId}")
    suspend fun updateAnalysis(@Path("sessionId") sessionId: String, @Body updatedAnalysisDTO: UpdatedAnalysisDTO)

    @GET("/analysis/{analysisId}/result")
    suspend fun getResultsById(@Path("analysisId") analysisId: String): AnalysisResultsDTO
}
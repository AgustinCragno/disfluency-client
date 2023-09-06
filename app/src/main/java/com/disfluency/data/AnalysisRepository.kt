package com.disfluency.data

import android.util.Log
import com.disfluency.api.DisfluencyAPI
import com.disfluency.api.dto.PracticeDTO
import com.disfluency.api.error.AnalysisNotFoundException
import com.disfluency.api.error.PatientNotFoundException
import com.disfluency.model.analysis.Analysis
import com.disfluency.model.analysis.AnalysisResults
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.File

class AnalysisRepository {

    suspend fun getAnalysisListByPatient(patientId: String): List<Analysis>{
        Log.i("analysis", "Retrieving analysis of patient: $patientId")
        try {
            val sessionList = DisfluencyAPI.patientService.getSessionsByPatientId(patientId)
            Log.i(
                "analysis",
                "Successfully retrieved ${sessionList.size} analysis of patient: $patientId"
            )
            return sessionList.map { it.asSession() }
        } catch (e: HttpException) {
            throw PatientNotFoundException(patientId)
        }
    }

    suspend fun getAnalysisResultsById(sessionId: String): AnalysisResults {
        Log.i("analysis", "Retrieving results of analysis: $sessionId")
        try {
            val results = DisfluencyAPI.analysisService.getResultsBySessionId(sessionId)
            Log.i("analysis", "Successfully retrieved results of analysis: $sessionId"
            )
            return results.asAnalysisResults()
        } catch (e: HttpException) {
            throw AnalysisNotFoundException(sessionId)
        }
    }

    suspend fun getSessionUrl(patientId: String): String {
        return DisfluencyAPI.patientService.getSessionPreSignedUrl(patientId).recordingUrl
    }

    suspend fun saveSession(url: String, audio: File): ResponseBody {
        Log . i ("S3--------", url)
        return DisfluencyAPI.exerciseService.uploadAudioToS3(
            url,
            audio.asRequestBody("audio/mpeg".toMediaTypeOrNull())
        )
    }

    suspend fun createSession(patientId: String, sessionUrl: String): Analysis {
        Log.i("analysis", "Creating session for patient: $patientId")
        try {
            /**
             * TODO: implement
             */
            val result = DisfluencyAPI.patientService.createSession(patientId, PracticeDTO(sessionUrl))
            Log.i("analysis", "Successfully created session of patient: $patientId")
            return result.asSession()
        } catch (e: HttpException){
            throw PatientNotFoundException(patientId)
        }
    }
}
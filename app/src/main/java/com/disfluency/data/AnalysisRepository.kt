package com.disfluency.data

import android.util.Log
import com.disfluency.api.error.PatientNotFoundException
import com.disfluency.data.mock.MockedData
import com.disfluency.model.analysis.Analysis
import retrofit2.HttpException

class AnalysisRepository {

    suspend fun getAnalysisListByPatient(patientId: String): List<Analysis>{
        Log.i("analysis", "Retrieving analysis of patient: $patientId")
        try {
            /**
             * TODO: implement
             */
            val result = listOf(MockedData.longAnalysis)
            Log.i("analysis", "Successfully retrieved ${result.size} analysis of patient: $patientId")
            return result
        } catch (e: HttpException){
            throw PatientNotFoundException(patientId)
        }
    }
}
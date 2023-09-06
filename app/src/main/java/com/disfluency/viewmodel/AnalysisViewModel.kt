package com.disfluency.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.disfluency.api.error.AnalysisNotFoundException
import com.disfluency.data.AnalysisRepository
import com.disfluency.model.analysis.Analysis
import com.disfluency.model.analysis.AnalysisResults
import kotlinx.coroutines.launch

class AnalysisViewModel : ViewModel() {

    private val analysisRepository = AnalysisRepository()

    private val patientAnalysisMap = mutableMapOf<String, MutableState<List<Analysis>>>()

    val patientAnalysis = mutableStateOf<List<Analysis>?>(null)
    val analysisResults = mutableStateOf<AnalysisResults?>(null)

    fun getAnalysisListByPatientId(patientId: String) = viewModelScope.launch {
//        patientAnalysisMap[patientId] = mutableStateOf(analysisRepository.getAnalysisListByPatient(patientId))
        patientAnalysis.value = analysisRepository.getAnalysisListByPatient(patientId)
    }

    fun analysisListOf(patientId: String): MutableState<List<Analysis>>? {
        return patientAnalysisMap[patientId]
    }

    fun getAnalysis(id: String): Analysis {
        return patientAnalysis.value?.find { it.id == id }
//        return patientAnalysisMap.values.flatMap { it.value }.find { it.id == id }
            ?: throw AnalysisNotFoundException(id)
    }

    fun getResults(id: String) = viewModelScope.launch {
        analysisResults.value = analysisRepository.getAnalysisResultsById(id)
    }
}
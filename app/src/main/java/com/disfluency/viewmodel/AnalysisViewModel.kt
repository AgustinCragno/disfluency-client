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

    val patientAnalysis = mutableStateOf<List<Analysis>?>(null)
    val analysisResults = mutableStateOf<AnalysisResults?>(null)

    fun getAnalysisListByPatientId(patientId: String, onComplete: () -> Unit = {}) = viewModelScope.launch {
        patientAnalysis.value = analysisRepository.getAnalysisListByPatient(patientId)
        onComplete.invoke()
    }

    fun getAnalysis(id: String): Analysis {
        return patientAnalysis.value?.find { it.id == id }
            ?: throw AnalysisNotFoundException(id)
    }

    fun getSessionIndex(analysis: Analysis): Int {
        return patientAnalysis.value!!.indexOf(analysis) + 1
    }

    fun getResults(id: String) = viewModelScope.launch {
        analysisResults.value = analysisRepository.getAnalysisResultsById(id)
    }
}
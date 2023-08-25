package com.disfluency.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.disfluency.data.ExerciseRepository
import com.disfluency.viewmodel.states.ConfirmationState
import kotlinx.coroutines.launch
import java.io.File

class RecordExerciseViewModel(context: Context) : RecordAudioViewModel(context) {

    private val exerciseRepository = ExerciseRepository()

    val uploadConfirmationState = mutableStateOf(ConfirmationState.DONE)

    private suspend fun uploadRecording(assignmentId: String, audioFile: File) {
        uploadConfirmationState.value = ConfirmationState.LOADING
        try {
            exerciseRepository.saveExercisePractice(assignmentId, audioFile)
            uploadConfirmationState.value = ConfirmationState.SUCCESS
        } catch (e: Exception){
            Log.d("record", "error uploading assignment $assignmentId :", e)
            uploadConfirmationState.value = ConfirmationState.ERROR
        }
    }

    fun uploadRecording(assignmentId: String) = viewModelScope.launch {
        outputFile?.let { uploadRecording(assignmentId, it) }
    }
}
package com.disfluency.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.disfluency.data.ExerciseRepository
import com.disfluency.viewmodel.states.ConfirmationState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import kotlin.random.Random

class RecordScreenViewModel() : ViewModel() {

    private val exerciseRepository = ExerciseRepository()

    val isMenuExtended = mutableStateOf(false)

    val audioAmplitudes: MutableList<Float> = randomWaves(35) as MutableList<Float>

    val uploadConfirmationState = mutableStateOf(ConfirmationState.DONE)

    private fun randomWaves(n: Int): List<Float> {
        return (1..n).map { Random.nextFloat() }
    }

    fun uploadRecording(assignmentId: String, audioFile: File) = viewModelScope.launch {
        uploadConfirmationState.value = ConfirmationState.LOADING
        try {
            exerciseRepository.saveExercisePractice(assignmentId, audioFile)
            uploadConfirmationState.value = ConfirmationState.SUCCESS
        } catch (e: Exception){
            Log.d("record", "error uploading assignment $assignmentId :", e)
            uploadConfirmationState.value = ConfirmationState.ERROR
        }
    }
}
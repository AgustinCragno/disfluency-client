package com.disfluency.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.disfluency.api.dto.ExerciseDTO
import com.disfluency.model.exercise.Exercise
import com.disfluency.viewmodel.states.ConfirmationState
import com.disfluency.worker.*
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.launch
import java.io.File

class RecordExerciseExampleViewModel(context: Context, private val lifecycleOwner: LifecycleOwner) : RecordAudioViewModel(context) {

    val uploadedExercise = mutableStateOf<Exercise?>(null)
    val uploadConfirmationState = mutableStateOf(ConfirmationState.DONE)

    private val workManager = WorkManager.getInstance(context)

    private fun dispatchFileUploadWorker(
        therapistId: String,
        title: String,
        instruction: String,
        phrase: String?,
        file: File
    ) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val inputData = Data.Builder()
            .putString(FILE_UPLOAD_THERAPIST_KEY, therapistId)
            .putString(FILE_UPLOAD_EXERCISE_TITLE_KEY, title)
            .putString(FILE_UPLOAD_EXERCISE_INSTRUCTION_KEY, instruction)
            .putString(FILE_UPLOAD_EXERCISE_PHRASE_KEY, phrase)
            .putString(FILE_UPLOAD_PATH_KEY, file.path)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<ExerciseUploadWorker>()
            .setInputData(inputData)
            .setConstraints(constraints)
            .build()

        Log.i("RecordExerciseExampleViewModel", "Preparing exercise upload worker")
        workManager.enqueueUniqueWork(ExerciseUploadWorker.WORK_NAME, ExistingWorkPolicy.APPEND_OR_REPLACE, workRequest)
        Log.i("RecordExerciseExampleViewModel", "Enqueued exercise upload worker")

        workManager.getWorkInfoByIdLiveData(workRequest.id).observe(lifecycleOwner) { workInfo ->

            uploadConfirmationState.value = workStateAsConfirmationState(workInfo.state)

            if (uploadConfirmationState.value == ConfirmationState.SUCCESS){
                val responseExerciseAsString = workInfo.outputData.getString(OUTPUT_EXERCISE)
                val responseExercise = ObjectMapper().readValue(responseExerciseAsString, ExerciseDTO::class.java)
                uploadedExercise.value = responseExercise.asExercise()
            }
        }
    }

    private fun workStateAsConfirmationState(state: WorkInfo.State): ConfirmationState {
        return when(state){
            WorkInfo.State.RUNNING -> ConfirmationState.LOADING
            WorkInfo.State.ENQUEUED -> ConfirmationState.LOADING
            WorkInfo.State.BLOCKED -> ConfirmationState.LOADING
            WorkInfo.State.SUCCEEDED -> ConfirmationState.SUCCESS
            WorkInfo.State.FAILED -> ConfirmationState.ERROR
            WorkInfo.State.CANCELLED -> ConfirmationState.ERROR
        }
    }

    fun uploadRecording(therapistId: String, title: String, instruction: String, phrase: String?) = viewModelScope.launch {
        outputFile?.let { dispatchFileUploadWorker(therapistId, title, instruction, phrase, it) }
    }
}
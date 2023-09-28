package com.disfluency.viewmodel.record

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.disfluency.viewmodel.states.ConfirmationState
import com.disfluency.worker.*
import kotlinx.coroutines.launch
import java.io.File

class RecordExerciseAssignmentViewModel(context: Context, private val lifecycleOwner: LifecycleOwner) : RecordAudioViewModel(context) {

    val uploadConfirmationState = mutableStateOf(ConfirmationState.DONE)

    private val workManager = WorkManager.getInstance(context)

    
    private fun dispatchFileUploadWorker(
        assignmentId: String,
        file: File
    ) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val inputData = Data.Builder()
            .putString(FILE_UPLOAD_ASSIGNMENT_KEY, assignmentId)
            .putString(FILE_UPLOAD_PATH_KEY, file.path)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<ExercisePracticeUploadWorker>()
            .setInputData(inputData)
            .setConstraints(constraints)
            .build()

        Log.i("RecordExerciseViewModel", "Preparing exercise upload worker")
        workManager.enqueueUniqueWork(ExercisePracticeUploadWorker.WORK_NAME, ExistingWorkPolicy.APPEND_OR_REPLACE, workRequest)
        Log.i("RecordExerciseViewModel", "Enqueued exercise upload worker")

        workManager.getWorkInfoByIdLiveData(workRequest.id).observe(lifecycleOwner) { workInfo ->
            uploadConfirmationState.value = workStateAsConfirmationState(workInfo.state)
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

    fun uploadRecording(assignmentId: String) = viewModelScope.launch {
        outputFile?.let { dispatchFileUploadWorker(assignmentId, it) }
    }
}
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
import java.time.LocalDateTime
import java.time.LocalTime

class RecordSessionViewModel(context: Context, private val lifecycleOwner: LifecycleOwner) : RecordAudioViewModel(context) {

    val uploadConfirmationState = mutableStateOf(ConfirmationState.DONE)

    private val workManager = WorkManager.getInstance(context)

    
    private fun dispatchFileUploadWorker(
        patientId: String,
        file: File
    ) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val inputData = Data.Builder()
            .putString(FILE_UPLOAD_ASSIGNMENT_KEY, patientId)
            .putString(FILE_UPLOAD_PATH_KEY, file.path)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<SessionUploadWorker>()
            .setInputData(inputData)
            .setConstraints(constraints)
            .build()

        val uniqueWorkName = SessionUploadWorker.WORK_NAME + patientId + LocalDateTime.now().toString()

        Log.i("RecordSessionViewModel", "Preparing session upload worker")
        workManager.enqueueUniqueWork(uniqueWorkName, ExistingWorkPolicy.REPLACE, workRequest)
        Log.i("RecordSessionViewModel", "Enqueued session upload worker")

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

    fun uploadRecording(patientId: String) = viewModelScope.launch {
        outputFile?.let { dispatchFileUploadWorker(patientId, it) }
    }
}
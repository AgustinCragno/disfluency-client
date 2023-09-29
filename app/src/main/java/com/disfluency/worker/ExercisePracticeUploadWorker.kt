package com.disfluency.worker

import android.content.Context
import androidx.work.WorkerParameters
import com.disfluency.data.ExerciseRepository
import okhttp3.ResponseBody
import java.io.File
import java.io.FileNotFoundException

class ExercisePracticeUploadWorker(appContext: Context, params: WorkerParameters) : FileUploadWorker(appContext, params) {

    private val exerciseRepository = ExerciseRepository()

    override suspend fun upload(): Result {
        val assignmentId = params.inputData.getString(FILE_UPLOAD_ASSIGNMENT_KEY)
        val filePath = params.inputData.getString(FILE_UPLOAD_PATH_KEY)

        assignmentId?.let {
            filePath?.let {
                val file = File(filePath)
                exerciseRepository.saveExercisePractice(assignmentId, file)
                return Result.success()
            }
        }

        throw FileNotFoundException()
    }

    companion object {
        const val WORK_NAME = "ExercisePracticeUploadWorker"
    }
}
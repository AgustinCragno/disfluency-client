package com.disfluency.worker

import android.content.Context
import androidx.work.WorkerParameters
import com.disfluency.data.AnalysisRepository
import okhttp3.ResponseBody
import okhttp3.internal.EMPTY_RESPONSE
import java.io.File
import java.io.FileNotFoundException

class SessionUploadWorker(appContext: Context, params: WorkerParameters) : FileUploadWorker(appContext, params) {

    private val analysisRepository = AnalysisRepository()

    override suspend fun upload(): Result {
        val patientId = params.inputData.getString(FILE_UPLOAD_ASSIGNMENT_KEY)
        val filePath = params.inputData.getString(FILE_UPLOAD_PATH_KEY)

        patientId?.let {
            filePath?.let {
                val file = File(filePath)
                val url = analysisRepository.getSessionUrl(patientId)
                val response = analysisRepository.saveSession(url, file)
                analysisRepository.createSession(patientId, url)
                return Result.success()
            }
        }

        throw FileNotFoundException()
    }

    companion object {
        const val WORK_NAME = "SessionUploadWorker"
    }
}
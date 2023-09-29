package com.disfluency.worker

import android.content.Context
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.disfluency.api.dto.ExerciseDTO
import com.disfluency.api.dto.NewExerciseDTO
import com.disfluency.data.ExerciseRepository
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.ResponseBody
import okhttp3.internal.EMPTY_RESPONSE
import java.io.File
import java.io.FileNotFoundException

class ExerciseUploadWorker(appContext: Context, params: WorkerParameters) : FileUploadWorker(appContext, params) {

    private val exerciseRepository = ExerciseRepository()

    override suspend fun upload(): Result {
        val therapistId = params.inputData.getString(FILE_UPLOAD_THERAPIST_KEY)

        val title = params.inputData.getString(FILE_UPLOAD_EXERCISE_TITLE_KEY)
        val instruction = params.inputData.getString(FILE_UPLOAD_EXERCISE_INSTRUCTION_KEY)
        val phrase = params.inputData.getString(FILE_UPLOAD_EXERCISE_PHRASE_KEY)
        val filePath = params.inputData.getString(FILE_UPLOAD_PATH_KEY)

        if (listOf(therapistId, title, instruction, filePath).any { it.isNullOrEmpty() })
            throw IllegalStateException("ExerciseUploadWorker parameters cannot be null: aborting upload")

        val url = exerciseRepository.getExerciseSampleUrl(therapistId!!)

        val newExerciseDTO = NewExerciseDTO(
            title = title!!,
            instruction = instruction!!,
            phrase = phrase,
            sampleRecordingUrl = url
        )

        val exercise = exerciseRepository.createExercise(therapistId, newExerciseDTO)
        val exerciseAsString = ObjectMapper().writeValueAsString(ExerciseDTO.from(exercise))

        return Result.success(
            workDataOf(
                OUTPUT_EXERCISE to exerciseAsString
            )
        )
    }

    companion object {
        const val WORK_NAME = "ExerciseUploadWorker"
    }
}
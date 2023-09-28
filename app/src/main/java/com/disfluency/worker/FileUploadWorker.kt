package com.disfluency.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.disfluency.R
import kotlinx.coroutines.delay
import okhttp3.ResponseBody
import java.time.LocalTime
import kotlin.random.Random

abstract class FileUploadWorker(private val appContext: Context, protected val params: WorkerParameters) : CoroutineWorker(appContext, params) {

    private val uploadChannelId = "FileUploadWorker"

    override suspend fun doWork(): Result {
        createNotificationChannel()
        startForegroundService()

        Log.d("FileUploadWorker", "File upload start time: ${LocalTime.now()}")
        return try {
            Log.i("FileUploadWorker", "uploading audio file")
            return upload()
        } catch (e: Exception){
            Log.i("FileUploadWorker", "an error occurred when uploading audio file")
            Result.failure()
        }
    }

    protected abstract suspend fun upload(): Result

    private suspend fun startForegroundService(){
        setForeground(
            ForegroundInfo(
                Random.nextInt(),
                NotificationCompat.Builder(appContext, uploadChannelId)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(applicationContext.getString(R.string.app_name))
                    .setContentText(applicationContext.getString(R.string.audio_upload_notification_content))
                    .setProgress(0,0,true)
                    .build()
            )
        )
    }

    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(
            uploadChannelId,
            "FileUploadWorker",
            NotificationManager.IMPORTANCE_DEFAULT,
        )

        val notificationManager: NotificationManager? =
            getSystemService(
                applicationContext,
                NotificationManager::class.java)

        notificationManager?.createNotificationChannel(
            notificationChannel
        )
    }

}
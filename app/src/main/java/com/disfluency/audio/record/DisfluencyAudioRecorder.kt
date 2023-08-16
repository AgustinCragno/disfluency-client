package com.disfluency.audio.record

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream

private const val MIN_AMPLITUDE_VALUE = 0f
private const val MAX_AMPLITUDE_VALUE = 7000f

private const val AMPLITUDE_SAMPLE_TIME = 50L

class DisfluencyAudioRecorder(private val context: Context) {

    private var recorder: MediaRecorder? = null

    private var amplitudeTrackerJob: Job? = null

    val audioAmplitudes = mutableStateListOf(MIN_AMPLITUDE_VALUE)

    val isRecording = mutableStateOf(false)

    private fun createRecorder(): MediaRecorder{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            MediaRecorder(context)
        } else MediaRecorder()
    }

    fun start(outputFile: File){
        createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(FileOutputStream(outputFile).fd)

            prepare()
            start()

            recorder = this
            isRecording.value = true

            amplitudeTrackerJob?.cancel()
            amplitudeTrackerJob = CoroutineScope(Dispatchers.Default).launch {
                while (recorder != null){
                    audioAmplitudes.add(getRecordingAmplitude())

                    delay(AMPLITUDE_SAMPLE_TIME)
                }
            }
        }
    }

    fun stop(){
        amplitudeTrackerJob?.cancel()
        amplitudeTrackerJob = null
        isRecording.value = false
        recorder?.stop()
        recorder?.reset()
        recorder?.release()
        recorder = null
    }

    private fun getRecordingAmplitude(): Float {
        return recorder?.maxAmplitude?.toFloat()?.div(MAX_AMPLITUDE_VALUE) ?: MIN_AMPLITUDE_VALUE
    }
}
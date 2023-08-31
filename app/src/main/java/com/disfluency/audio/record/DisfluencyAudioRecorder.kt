package com.disfluency.audio.record

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.*
import java.io.File
import kotlin.math.roundToInt

private const val MIN_AMPLITUDE_VALUE = 0F
//const val MAX_AMPLITUDE_VALUE = 32762F
const val MAX_AMPLITUDE_VALUE = 9000F

private const val MAX_AMPLITUDE_VALUE_INT = 11

private const val AMPLITUDE_SAMPLE_TIME = 50L

class DisfluencyAudioRecorder(private val context: Context) {

    private var recorder: MediaRecorder? = null

    private var amplitudeTrackerJob: Job? = null

    private val audioAmplitudes = mutableStateOf<List<Float>>(listOf())

    val isRecording = mutableStateOf(false)
    val hasRecorded = mutableStateOf(false)

    private fun createRecorder(): MediaRecorder{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            MediaRecorder(context)
        } else MediaRecorder()
    }

    fun start(outputFile: File){
        createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(outputFile)

            prepare()
            start()

            recorder = this
            isRecording.value = true
            hasRecorded.value = false

            amplitudeTrackerJob?.cancel()
            amplitudeTrackerJob = CoroutineScope(Dispatchers.Default).launch {
                audioAmplitudes.value = listOf()

                while (recorder != null){
                    audioAmplitudes.value = audioAmplitudes.value + getRecordingAmplitude()
                    delay(AMPLITUDE_SAMPLE_TIME)
                }
            }
        }
    }

    fun stop(){
        amplitudeTrackerJob?.cancel()
        amplitudeTrackerJob = null

        isRecording.value = false
        hasRecorded.value = true

        recorder?.stop()
        recorder?.reset()
        recorder?.release()
        recorder = null
    }

    fun reset(){
        amplitudeTrackerJob?.cancel()
        amplitudeTrackerJob = null
        isRecording.value = false
        hasRecorded.value = false
        audioAmplitudes.value = listOf()
    }

    private fun getRecordingAmplitude(): Float {
//        val amplitudeDb = recorder?.maxAmplitude?.let { it -> 20 * log10(abs(it).toFloat()) }
        return recorder?.maxAmplitude?.toFloat()/*?.coerceAtMost(MAX_AMPLITUDE_VALUE)*//*?.div(MAX_AMPLITUDE_VALUE)*/ ?: MIN_AMPLITUDE_VALUE
    }

    fun amplitudesAsFloat(): List<Float> {
        return audioAmplitudes.value
    }

    /**
     * Transforms a list of recorder-measured amplitudes in the range of 0-MAX_AMPLITUDE_VALUE (Float) to
     * a list of Integers in the range of 0-MAX_AMPLITUDE_VALUE_INT that can be used by the AudioWaveformComponent
     */
    private fun transformAmplitudes(amplitudes: List<Float>): List<Int> {
        return amplitudes.map { f -> (f / MAX_AMPLITUDE_VALUE * MAX_AMPLITUDE_VALUE_INT).roundToInt() }
    }

    fun amplitudesAsInt(): List<Int> {
        return transformAmplitudes(audioAmplitudes.value)
    }
}
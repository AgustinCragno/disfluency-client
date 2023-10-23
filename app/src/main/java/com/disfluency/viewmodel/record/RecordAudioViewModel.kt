package com.disfluency.viewmodel.record

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.disfluency.audio.playback.DisfluencyAudioFilePlayer
import com.disfluency.audio.record.DisfluencyAudioRecorder
import kotlinx.coroutines.launch
import java.io.File

const val LOCAL_RECORD_FILE = "disfluency_exercise_recording.mp3"


open class RecordAudioViewModel(context: Context) : ViewModel() {

    val audioPlayer = DisfluencyAudioFilePlayer(context)
    val audioRecorder = DisfluencyAudioRecorder(context)
    protected var outputFile: File? = null


    fun start(context: Context) = viewModelScope.launch {
        File(context.cacheDir, LOCAL_RECORD_FILE).also {
            audioRecorder.start(it)
            outputFile = it
        }
    }

    fun stop() = viewModelScope.launch {
        audioRecorder.stop()
        audioPlayer.load(LOCAL_RECORD_FILE)
    }

    fun delete() = viewModelScope.launch {
        audioRecorder.reset()
        outputFile?.apply { delete() }
        outputFile = null
    }

    fun play() = viewModelScope.launch {
        if (!audioPlayer.isPlaying()){
            audioPlayer.play()
        } else {
            audioPlayer.pause()
        }
    }

    fun playerProgress(): Float {
        return audioPlayer.position() / audioPlayer.duration()
    }

    fun seekProgress(progressChange: Float) {
        audioPlayer.seekTo(audioPlayer.duration() * progressChange)
    }

    fun isPlaybackReady(): Boolean {
        return audioRecorder.hasRecorded.value && audioPlayer.asyncReady()
    }

    fun isRecordingValid(): Boolean {
        return audioRecorder.hasRecorded.value && audioRecorder.amplitudesAsInt().any { it > 1 }
    }
}
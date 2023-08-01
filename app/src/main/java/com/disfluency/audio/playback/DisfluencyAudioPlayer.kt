package com.disfluency.audio.playback

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException

abstract class DisfluencyAudioPlayer(private val context: Context) {

    private var player: MediaPlayer? = null

    private var position: MutableState<Int> = mutableStateOf(0)
    private var totalDuration: MutableState<Int> = mutableStateOf(0)
    private var isPlaying: MutableState<Boolean> = mutableStateOf(false)
    private var asyncReady: MutableState<Boolean> = mutableStateOf(false)

    private var progressTrackerJob: Job? = null

    protected abstract fun loadUri(media: String): Uri

    fun load(media: String){
        MediaPlayer().apply {
            player = this

            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )

            try {
                setDataSource(context, loadUri(media))
                prepareAsync()
                setOnPreparedListener {
                    asyncReady.value = true
                    totalDuration.value = duration
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun play(){
        isPlaying.value = true
        player?.start()
        evaluateProgress()
    }

    fun pause(){
        isPlaying.value = false
        player?.pause()
        progressTrackerJob?.cancel()
    }

    fun stop(){
        progressTrackerJob?.cancel()
        isPlaying.value = false

        seekTo(0)

        player?.apply {
            pause()
        }
    }

    fun seekTo(millis: Int){
        if (millis >= totalDuration.value && !isPlaying()){
            return
        }

        if (millis >= totalDuration.value) {
            position.value = totalDuration.value
            stop()
        }else {
            player?.seekTo(millis)
            position.value = millis
        }
    }

    fun release(){
        isPlaying.value = false
        progressTrackerJob?.cancel()
        player?.release()
        player = null
    }

    private fun evaluateProgress(){
        progressTrackerJob?.cancel()
        progressTrackerJob = CoroutineScope(Dispatchers.Default).launch {

            while (isPlaying()){
                position.value = player!!.currentPosition

                if (position.value >= totalDuration.value) stop()
            }
        }
    }

    fun position(): Int {
        return position.value
    }

    fun duration(): Int {
        return totalDuration.value
    }

    fun isPlaying(): Boolean {
        return isPlaying.value
    }

    fun asyncReady(): Boolean {
        return asyncReady.value
    }
}
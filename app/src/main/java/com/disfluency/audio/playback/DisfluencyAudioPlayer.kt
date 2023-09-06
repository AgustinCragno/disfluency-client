package com.disfluency.audio.playback

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import linc.com.amplituda.Amplituda
import linc.com.amplituda.Cache
import linc.com.amplituda.callback.AmplitudaErrorListener
import java.io.IOException

abstract class DisfluencyAudioPlayer(private val context: Context) {

    private var player: MediaPlayer? = null

    private var position: MutableState<Float> = mutableStateOf(0f)
    private var totalDuration: MutableState<Float> = mutableStateOf(0.1f)
    private var isPlaying: MutableState<Boolean> = mutableStateOf(false)
    private var asyncReady: MutableState<Boolean> = mutableStateOf(false)

    private val audioAmplitudes = mutableStateOf<List<Int>>(listOf())

    private var progressTrackerJob: Job? = null

    protected abstract fun loadUri(media: String): Uri
    protected abstract fun loadPath(media: String): String

    fun load(media: String){
        MediaPlayer().apply {
            Log.i("DisfluencyAudioPlayer", "loading media")
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
                    totalDuration.value = duration.toFloat()
                }
                retrieveAudioAmplitudes(loadPath(media), context)
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

        seekTo(0f)

        player?.apply {
            pause()
        }
    }

    fun seekTo(millis: Float){
        if (millis >= totalDuration.value && !isPlaying()){
            return
        }

        if (millis >= totalDuration.value) {
            position.value = totalDuration.value
            stop()
        }else {
            player?.seekTo(millis.toInt())
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
                position.value = player!!.currentPosition.toFloat()

                if (position.value >= totalDuration.value) stop()
            }
        }
    }

    private fun retrieveAudioAmplitudes(audio: String, context: Context){
        CoroutineScope(Dispatchers.Default).launch {
            audioAmplitudes.value = Amplituda(context).processAudio(audio, Cache.withParams(
                Cache.REUSE))
                .get(AmplitudaErrorListener {
                    it.printStackTrace()
                })
                .amplitudesAsList()
        }
    }

    fun position(): Float {
        return position.value
    }

    fun duration(): Float {
        return totalDuration.value
    }

    fun isPlaying(): Boolean {
        return isPlaying.value
    }

    fun asyncReady(): Boolean {
        return asyncReady.value && amplitudes().isNotEmpty()
    }

    fun amplitudes(): List<Int> {
        return audioAmplitudes.value
    }
}
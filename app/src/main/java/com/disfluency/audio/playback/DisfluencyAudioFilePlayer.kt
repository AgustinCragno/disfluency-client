package com.disfluency.audio.playback

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import java.io.File

class DisfluencyAudioFilePlayer(private val context: Context) : DisfluencyAudioPlayer(context) {

    override fun loadUri(media: String): Uri {
        return File(context.cacheDir, media).toUri()
    }

    override fun loadPath(media: String): String {
        return File(context.cacheDir, media).path
    }
}
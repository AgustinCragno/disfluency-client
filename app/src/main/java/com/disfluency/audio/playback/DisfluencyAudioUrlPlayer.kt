package com.disfluency.audio.playback

import android.content.Context
import android.net.Uri

class DisfluencyAudioUrlPlayer(context: Context) : DisfluencyAudioPlayer(context) {

    override fun loadUri(media: String): Uri {
        return Uri.parse(media)
    }

    override fun loadPath(media: String): String {
        return media
    }
}
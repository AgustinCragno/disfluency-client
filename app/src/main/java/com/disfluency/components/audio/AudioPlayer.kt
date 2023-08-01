package com.disfluency.components.audio

import android.content.Context
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import com.disfluency.audio.playback.DisfluencyAudioFilePlayer
import com.disfluency.audio.playback.DisfluencyAudioPlayer
import com.disfluency.audio.playback.DisfluencyAudioUrlPlayer
import com.disfluency.utilities.format.formatMillisecondsAsMinuteAndSeconds

enum class AudioMediaType(val getPlayer: (Context) -> DisfluencyAudioPlayer){
    FILE({ context -> DisfluencyAudioFilePlayer(context) }),
    URL({ context -> DisfluencyAudioUrlPlayer(context) })
}

@Composable
fun AudioPlayer(url: String, type: AudioMediaType){
    AudioPlayer(url = url, audioPlayer = type.getPlayer(LocalContext.current))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioPlayer(url: String, audioPlayer: DisfluencyAudioPlayer){
    audioPlayer.load(url)

    DisposableEffect(Lifecycle.Event.ON_STOP) {
        onDispose {
            audioPlayer.release()
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(10.dp)),
        color = MaterialTheme.colorScheme.surfaceTint
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                modifier = Modifier.size(40.dp),
                contentPadding = PaddingValues(1.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                onClick = {
                    if (!audioPlayer.isPlaying()){
                        audioPlayer.play()
                    } else {
                        audioPlayer.pause()
                    }
                },
                enabled = audioPlayer.asyncReady()
            ) {
                if (audioPlayer.isPlaying())
                    Icon(imageVector = Icons.Filled.Pause, contentDescription = "Pause")
                else
                    Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = "Play")
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Slider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CircleShape),
                    value = audioPlayer.position().toFloat(),
                    valueRange = 0f..audioPlayer.duration().toFloat(),
                    onValueChange = { sliderValue ->
                        audioPlayer.seekTo(sliderValue.toInt())
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    thumb = {
                        SliderDefaults.Thumb(
                            interactionSource = MutableInteractionSource(),
                            modifier = Modifier.offset(x = 5.dp, y = 5.5.dp),
                            thumbSize = DpSize(10.dp,10.dp)
                        )
                    }
                )
                
                Text(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .wrapContentSize(align = Alignment.BottomStart),
                    text = formatMillisecondsAsMinuteAndSeconds(audioPlayer.position().toLong()),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                )
            }
            
        }
    }
}

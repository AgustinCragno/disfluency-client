package com.disfluency.components.audio

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import com.disfluency.audio.playback.DisfluencyAudioFilePlayer
import com.disfluency.audio.playback.DisfluencyAudioPlayer
import com.disfluency.audio.playback.DisfluencyAudioUrlPlayer
import com.disfluency.ui.theme.DisfluencyTheme
import com.disfluency.utilities.format.formatMillisecondsAsMinuteAndSeconds
import com.linc.audiowaveform.model.AmplitudeType

enum class AudioMediaType(val getPlayer: (Context) -> DisfluencyAudioPlayer){
    FILE({ context -> DisfluencyAudioFilePlayer(context) }),
    URL({ context -> DisfluencyAudioUrlPlayer(context) })
}

@Composable
fun AudioPlayer(
    url: String,
    type: AudioMediaType,
    modifier: Modifier = Modifier,
    playButtonColor: Color = MaterialTheme.colorScheme.secondaryContainer
){
    AudioPlayer(url = url, audioPlayer = type.getPlayer(LocalContext.current), modifier, playButtonColor)
}

@Composable
fun AudioPlayer(
    url: String,
    audioPlayer: DisfluencyAudioPlayer,
    modifier: Modifier = Modifier,
    playButtonColor: Color = MaterialTheme.colorScheme.secondaryContainer
){
    LaunchedEffect(Unit){
        audioPlayer.load(url)
    }

    DisposableEffect(Lifecycle.Event.ON_STOP) {
        onDispose {
            audioPlayer.release()
        }
    }

    AudioPlayer(audioPlayer = audioPlayer, modifier = modifier, playButtonColor = playButtonColor)
}

@Composable
fun AudioPlayer(
    audioPlayer: DisfluencyAudioPlayer,
    modifier: Modifier = Modifier,
    playButtonColor: Color = MaterialTheme.colorScheme.secondaryContainer
){
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        ){
            AudioWaveformCustom(
                modifier = Modifier.fillMaxSize(),
                amplitudes = audioPlayer.amplitudes(),
                spikeHeight = 80.dp,
                progress = audioPlayer.position() / audioPlayer.duration(),
                onProgressChange = { progressChange ->
                    audioPlayer.seekTo(audioPlayer.duration() * progressChange)
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatMillisecondsAsMinuteAndSeconds(audioPlayer.position().toLong()),
                style = MaterialTheme.typography.labelSmall,
                color = Color.LightGray
            )

            Button(
                modifier = Modifier.size(40.dp),
                contentPadding = PaddingValues(1.dp),
                colors = ButtonDefaults.buttonColors(containerColor = playButtonColor),
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

            Text(
                text = formatMillisecondsAsMinuteAndSeconds(audioPlayer.duration().toLong()),
                style = MaterialTheme.typography.labelSmall,
                color = Color.LightGray,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

@Composable
fun CompactAudioPlayer(url: String, type: AudioMediaType, modifier: Modifier = Modifier){
    CompactAudioPlayer(url = url, audioPlayer = type.getPlayer(LocalContext.current), modifier)
}

@Composable
fun CompactAudioPlayer(
    url: String,
    audioPlayer: DisfluencyAudioPlayer,
    modifier: Modifier = Modifier
){
    LaunchedEffect(Unit){
        audioPlayer.load(url)
    }

    DisposableEffect(Lifecycle.Event.ON_STOP) {
        onDispose {
            audioPlayer.release()
        }
    }

    CompactAudioPlayer(audioPlayer = audioPlayer, modifier = modifier)
}

@Composable
fun CompactAudioPlayer(audioPlayer: DisfluencyAudioPlayer, modifier: Modifier = Modifier){

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            modifier = Modifier.size(40.dp),
            contentPadding = PaddingValues(1.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSecondaryContainer),
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

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier.fillMaxSize()
        ){
            AudioWaveformCustom(
                modifier = Modifier.fillMaxSize().align(Alignment.Center),
                amplitudes = audioPlayer.amplitudes(),
                spikeHeight = 30.dp,
                progress = audioPlayer.position() / audioPlayer.duration(),
                onProgressChange = { progressChange ->
                    audioPlayer.seekTo(audioPlayer.duration() * progressChange)
                }
            )

            Row(
                Modifier.fillMaxWidth().align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatMillisecondsAsMinuteAndSeconds(audioPlayer.position().toLong()),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(start = 3.dp)
                )

                Text(
                    text = formatMillisecondsAsMinuteAndSeconds(audioPlayer.duration().toLong()),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun AudioPlayerPreview(){
    DisfluencyTheme() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val url = "https://pf5302.s3.us-east-2.amazonaws.com/audios/toquesligeros.mp3"
            AudioPlayer(url = url, type = AudioMediaType.URL)
        }
    }
}
package com.disfluency.screens.therapist.exercises

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import com.disfluency.audio.playback.DisfluencyAudioUrlPlayer
import com.disfluency.components.audio.AudioPlayer
import com.disfluency.model.Exercise

@Composable
fun ExerciseDetailScreen(exercise: Exercise){
    var expanded by remember { mutableStateOf(true) }

    val audioPlayer = DisfluencyAudioUrlPlayer(LocalContext.current)

    LaunchedEffect(Unit){
        audioPlayer.load(exercise.sampleRecordingUrl)
    }

    DisposableEffect(Lifecycle.Event.ON_STOP) {
        onDispose {
            audioPlayer.release()
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
            )
            .padding(16.dp)
            .clickable { expanded = expanded.not() },
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondary),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = exercise.title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )

            AnimatedVisibility(
                visible = expanded,
                exit = fadeOut(tween(200)) + shrinkVertically(tween(400))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        thickness = 3.dp,
                        color = Color.Black.copy(alpha = 0.3f)
                    )

                    Text(
                        text = exercise.instruction,
                        color = Color.White,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )

                    AudioPlayer(
                        audioPlayer = audioPlayer,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
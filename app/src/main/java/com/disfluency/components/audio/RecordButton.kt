package com.disfluency.components.audio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.disfluency.audio.record.DisfluencyAudioRecorder
import java.io.File

const val ANIMATION_TIME = 200

@Composable
fun RecordAudioButton(
    onClick: () -> Unit,
    onRelease: () -> Unit,
    onSend: () -> Unit,
    onCancel: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    var startedRecording by remember { mutableStateOf(false) }

    val hasRecorded = (!isPressed && startedRecording)

    val animatedButtonColor = animateColorAsState(
        targetValue = if (isPressed) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.secondary,
        animationSpec = tween(ANIMATION_TIME, 0, LinearEasing)
    )

    val animateSize = animateDpAsState(
        targetValue = if (isPressed) 75.dp else 50.dp,
        animationSpec = tween(ANIMATION_TIME, 0, LinearEasing)
    )

    val animateSecondaryButtonsAlpha = animateFloatAsState(
        targetValue = if (hasRecorded) 1f else 0f,
        animationSpec = tween(ANIMATION_TIME, 0, LinearEasing)
    )


    val reset = {
        startedRecording = false
    }

    Row(
        modifier = Modifier
            .height(75.dp)
            .width(240.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            modifier = Modifier
                .size(50.dp)
                .alpha(animateSecondaryButtonsAlpha.value),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            border = BorderStroke(2.dp, Color.Red),
            contentPadding = PaddingValues(1.dp),
            enabled = hasRecorded,
            onClick = {
                onCancel()
                reset()
            }
        ) {
            Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Delete", tint = Color.Red)
        }

        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(containerColor = animatedButtonColor.value),
            contentPadding = PaddingValues(1.dp),
            modifier = Modifier.size(animateSize.value),
            interactionSource = interactionSource,
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary),
            enabled = !hasRecorded
        ) {
            if (isPressed) {

                if (!startedRecording){
                    onClick()
                    startedRecording = true
                }

                //Use if + DisposableEffect to wait for the press action is completed
                DisposableEffect(Unit) {
                    onDispose {
                        onRelease()
                    }
                }
            }

            Icon(imageVector = Icons.Outlined.Mic, contentDescription = "Record", tint = MaterialTheme.colorScheme.onSecondaryContainer)
        }

        Button(
            modifier = Modifier
                .size(50.dp)
                .alpha(animateSecondaryButtonsAlpha.value),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.onSecondaryContainer),
            contentPadding = PaddingValues(1.dp),
            enabled = hasRecorded,
            onClick = onSend
        ) {
            Icon(imageVector = Icons.Outlined.Send, contentDescription = "Send", tint = MaterialTheme.colorScheme.onSecondaryContainer)
        }
    }
}

@Composable
fun LocalFileRecordButton(
    audioRecorder: DisfluencyAudioRecorder,
    filePath: String,
    onSend: (File) -> Unit
){
    val context = LocalContext.current

    var audioFile: File? = null

    Row(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RecordAudioButton(
            onClick = {
                File(context.cacheDir, filePath).also {
                    audioRecorder.start(it)
                    audioFile = it
                }
            },
            onRelease = {
                audioRecorder.stop()
            },
            onSend = {
                File(context.cacheDir, filePath).let(onSend)
            },
            onCancel = {
                audioRecorder.audioAmplitudes.clear()
                audioFile?.apply { delete() }
                audioFile = null
            }
        )
    }
}
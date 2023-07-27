package com.disfluency.screens.therapist

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.disfluency.R
import com.disfluency.components.audio.AudioMediaType
import com.disfluency.components.audio.AudioPlayer
import com.disfluency.model.Exercise

@Composable
fun ExerciseDetailScreen(exercise: Exercise){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = exercise.title,
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = exercise.instruction,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp),
            )

            exercise.phrase?.let{
                Text(
                    text = stringResource(R.string.repeat_the_following_phrase),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 4.dp, top = 24.dp)
                )

                Text(
                    text = "\"${it}\"",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    textAlign = TextAlign.Center,
                    fontStyle = FontStyle.Italic
                )
            }
        }

        ExampleRecording(sampleAudioUrl = exercise.sampleRecordingUrl, subtitle = stringResource(R.string.example_generated_by_professional))
    }
}

@Composable
fun ExampleRecording(sampleAudioUrl: String, subtitle: String? = null){
    Column(modifier = Modifier.padding(top = 16.dp)) {
        subtitle?.let {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                text = it,
                style = MaterialTheme.typography.labelMedium
            )
        }

        AudioPlayer(url = sampleAudioUrl, type = AudioMediaType.URL)
    }
}
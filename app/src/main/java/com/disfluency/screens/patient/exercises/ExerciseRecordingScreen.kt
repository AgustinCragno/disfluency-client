package com.disfluency.screens.patient.exercises

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.disfluency.R
import com.disfluency.components.audio.AudioLiveWaveform
import com.disfluency.components.audio.AudioMediaType
import com.disfluency.components.audio.AudioPlayer
import com.disfluency.components.audio.AudioWaveformCustom
import com.disfluency.components.sheet.BottomSheetTitleContent
import com.disfluency.components.sheet.RecordScreenSheetScaffold
import com.disfluency.model.exercise.Exercise
import com.disfluency.model.exercise.ExerciseAssignment
import com.disfluency.navigation.routing.Route
import com.disfluency.ui.theme.DisfluencyTheme
import com.disfluency.viewmodel.ExercisesViewModel
import com.disfluency.viewmodel.record.RecordAudioViewModel
import com.disfluency.viewmodel.record.RecordExerciseAssignmentViewModel
import com.disfluency.viewmodel.states.ConfirmationState
import java.time.LocalDate


@Composable
fun RecordExerciseScreen(
    assignmentId: String,
    navController: NavHostController,
    exercisesViewModel: ExercisesViewModel,
    recordViewModel: RecordExerciseAssignmentViewModel
){
    val assignment = remember {
        mutableStateOf<ExerciseAssignment?>(null)
    }

    LaunchedEffect(Unit){
        recordViewModel.audioRecorder.reset()
        assignment.value = exercisesViewModel.getAssignmentById(assignmentId)
    }

    DisposableEffect(Lifecycle.Event.ON_STOP){
        onDispose {
            recordViewModel.audioPlayer.release()
        }
    }

    assignment.value?.let {
        RecordExercise(
            assignmentId = it.id,
            exercise = it.exercise,
            navController = navController,
            recordViewModel = recordViewModel
        )
    }

    if (recordViewModel.uploadConfirmationState.value == ConfirmationState.LOADING) {
        LaunchedEffect(Unit){
            navController.popBackStack()
            navController.navigate(Route.Patient.RecordConfirmation.path)
        }
    }
}

@Composable
private fun RecordExercise(
    assignmentId: String,
    exercise: Exercise,
    navController: NavHostController,
    recordViewModel: RecordExerciseAssignmentViewModel
){
    RecordScreenSheetScaffold(
        title = stringResource(R.string.practice),
        navController = navController,
        recordViewModel = recordViewModel,
        onSend = { recordViewModel.uploadRecording(assignmentId) },
        mainContent = {
            ExercisePhrasePanel(
                exercise = exercise,
                modifier = Modifier.fillMaxSize(),
                viewModel = recordViewModel
            )
        },
        sheetContent = { animateTitlePadding ->
            ExerciseInstructionsPanel(
                exercise = exercise,
                animatePadding = animateTitlePadding
            )
        }
    )
}

@Composable
private fun ExerciseInstructionsPanel(
    exercise: Exercise,
    animatePadding: State<Float>
){
    BottomSheetTitleContent(
        animatePadding = animatePadding,
        title = exercise.title
    ) {
        Text(
            text = exercise.instruction,
            color = Color.White,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        AudioPlayerPanel(exercise)
    }
}

@Composable
private fun AudioPlayerPanel(
    exercise: Exercise
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AudioPlayer(
            url = exercise.sampleRecordingUrl,
            type = AudioMediaType.URL,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun ExercisePhrasePanel(
    exercise: Exercise,
    modifier: Modifier = Modifier.fillMaxSize(),
    viewModel: RecordAudioViewModel
){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = exercise.phrase ?: exercise.instruction,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        RecordingVisualizer(viewModel)
    }
}

@Composable
private fun RecordingVisualizer(viewModel: RecordAudioViewModel){
    val transitionLength = 800

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 32.dp + 24.dp),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = !viewModel.isPlaybackReady(),
            exit = shrinkHorizontally(
                shrinkTowards = Alignment.CenterHorizontally,
                animationSpec = tween(transitionLength)
            )
        ) {
            AudioLiveWaveform(
                amplitudes = viewModel.audioRecorder.amplitudesAsFloat(), spikeHeight = 80.dp
            )
        }

        AnimatedVisibility(
            visible = viewModel.isPlaybackReady(),
            enter = expandHorizontally(
                expandFrom = Alignment.CenterHorizontally,
                animationSpec = tween(
                    durationMillis = transitionLength,
                    delayMillis = transitionLength
                )
            ),
            exit = shrinkHorizontally(
                shrinkTowards = Alignment.CenterHorizontally,
                animationSpec = tween(transitionLength)
            )
        ) {
            AudioWaveformCustom(
                modifier = Modifier.fillMaxSize(),
                amplitudes = viewModel.audioRecorder.amplitudesAsInt(),
                spikeHeight = 80.dp,
                progress = viewModel.playerProgress(),
                onProgressChange = {
                    viewModel.seekProgress(it)
                }
            )
        }
    }
}


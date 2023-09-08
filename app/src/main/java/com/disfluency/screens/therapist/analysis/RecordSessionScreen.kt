package com.disfluency.screens.therapist.analysis

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.disfluency.components.audio.AudioLiveWaveform
import com.disfluency.components.audio.AudioWaveformCustom
import com.disfluency.components.button.RecordButton
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.BackNavigationScaffold
import com.disfluency.viewmodel.AnalysisViewModel
import com.disfluency.viewmodel.RecordSessionViewModel
import com.disfluency.viewmodel.states.ConfirmationState

@Composable
fun RecordSessionScreen(patientId: String, navController: NavHostController, viewModel: AnalysisViewModel, recordViewModel: RecordSessionViewModel){
    DisposableEffect(Lifecycle.Event.ON_STOP){
        onDispose {
            recordViewModel.audioRecorder.reset()
            recordViewModel.audioPlayer.release()
        }
    }

    RecordSession(
        patientId = patientId,
        navController = navController,
        viewModel = viewModel,
        recordViewModel = recordViewModel
    )

    if (recordViewModel.uploadConfirmationState.value == ConfirmationState.LOADING) {
        LaunchedEffect(Unit){
            navController.popBackStack()
            navController.navigate(Route.Therapist.NewSessionConfirmation.routeTo(patientId))
        }
    }
}

@Composable
private fun RecordSession(patientId: String, navController: NavHostController, viewModel: AnalysisViewModel, recordViewModel: RecordSessionViewModel){
    val context = LocalContext.current

    BackNavigationScaffold(
        title = "Nueva Sesion",
        navController = navController
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            val isMenuExtended = remember { mutableStateOf(false) }

            RecordButton(
                modifier = Modifier.offset(y = 150.dp),
                isMenuExtended = isMenuExtended,
                viewModel = recordViewModel,
                onPress = {
                    recordViewModel.start(context)
                },
                onRelease = {
                    recordViewModel.stop()
                },
                onDelete = {
                    recordViewModel.delete()
                },
                onSend = {
                    recordViewModel.uploadRecording(patientId)
                },
                onPlay = {
                    recordViewModel.play()
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .offset(y = -50.dp)
            ){
                RecordingVisualizer(viewModel = recordViewModel)
            }
        }
    }
}

@Composable
private fun RecordingVisualizer(viewModel: RecordSessionViewModel){
    val transitionLength = 800

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
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
                amplitudes = viewModel.audioRecorder.amplitudesAsFloat(), spikeHeight = 250.dp
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

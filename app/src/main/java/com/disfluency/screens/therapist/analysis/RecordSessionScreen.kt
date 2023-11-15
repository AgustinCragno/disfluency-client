package com.disfluency.screens.therapist.analysis

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.audio.AudioLiveWaveform
import com.disfluency.components.audio.AudioWaveformCustom
import com.disfluency.components.sheet.BottomSheetTitleContent
import com.disfluency.components.sheet.RecordScreenSheetScaffold
import com.disfluency.model.user.Patient
import com.disfluency.navigation.routing.Route
import com.disfluency.utilities.format.formatLocalDate
import com.disfluency.viewmodel.AnalysisViewModel
import com.disfluency.viewmodel.record.RecordSessionViewModel
import com.disfluency.viewmodel.states.ConfirmationState
import java.time.LocalDate

@Composable
fun RecordSessionScreen(
    patient: Patient,
    sessionNumber: Int,
    navController: NavHostController,
    recordViewModel: RecordSessionViewModel
){
    DisposableEffect(Lifecycle.Event.ON_STOP){
        onDispose {
            recordViewModel.audioRecorder.reset()
            recordViewModel.audioPlayer.release()
        }
    }

    RecordSession(
        patient = patient,
        sessionNumber = sessionNumber,
        navController = navController,
        recordViewModel = recordViewModel
    )

    if (recordViewModel.uploadConfirmationState.value == ConfirmationState.LOADING) {
        LaunchedEffect(Unit){
            navController.popBackStack()
            navController.navigate(Route.Therapist.NewSessionConfirmation.routeTo(patient.id))
        }
    }
}

@Composable
private fun RecordSession(
    patient: Patient,
    sessionNumber: Int,
    navController: NavHostController,
    recordViewModel: RecordSessionViewModel
){

    RecordScreenSheetScaffold(
        title = stringResource(R.string.new_session),
        navController = navController,
        recordViewModel = recordViewModel,
        onSend = { recordViewModel.uploadRecording(patient.id) },
        mainContent = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                PatientSessionLabel(fullName = patient.fullName(), sessionNumber = sessionNumber)

                Spacer(modifier = Modifier.height(32.dp))

                RecordingVisualizer(viewModel = recordViewModel)

                Spacer(modifier = Modifier.height(32.dp))
            }
        },
        sheetContent = { animateTitlePadding ->
            BottomSheetTitleContent(animatePadding = animateTitlePadding, title = stringResource(R.string.disfluency_analisys)) {
                AnalysisGuidePanel()
            }
        }
    )
}

@Composable
private fun PatientSessionLabel(fullName: String, sessionNumber: Int){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = fullName,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp
        )

        Row(
            modifier = Modifier.wrapContentWidth()
        ) {
            Text(text = "Sesion #$sessionNumber")

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = formatLocalDate(LocalDate.now()),
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun RecordingVisualizer(viewModel: RecordSessionViewModel){
    val transitionLength = 800
    val height = 100.dp

    val exitTransition = shrinkHorizontally(
        shrinkTowards = Alignment.CenterHorizontally,
        animationSpec = tween(transitionLength)
    )

    val enterTransition = expandHorizontally(
        expandFrom = Alignment.CenterHorizontally,
        animationSpec = tween(
            durationMillis = transitionLength,
            delayMillis = transitionLength
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .padding(horizontal = 32.dp + 24.dp),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = !viewModel.audioRecorder.hasRecorded.value && !viewModel.audioRecorder.isRecording.value,
            enter = enterTransition,
            exit = fadeOut()
        ) {
            Text(
                text = stringResource(R.string.mantain_pressed_to_record_session),
                color = Color.LightGray,
                textAlign = TextAlign.Center
            )
        }

        AnimatedVisibility(
            visible = !viewModel.isPlaybackReady(),
            enter = enterTransition,
            exit = exitTransition
        ) {
            AudioLiveWaveform(
                amplitudes = viewModel.audioRecorder.amplitudesAsFloat(), spikeHeight = height
            )
        }

        AnimatedVisibility(
            visible = viewModel.isPlaybackReady(),
            enter = enterTransition,
            exit = exitTransition
        ) {
            AudioWaveformCustom(
                modifier = Modifier.fillMaxSize(),
                amplitudes = viewModel.audioRecorder.amplitudesAsInt(),
                spikeHeight = height,
                progress = viewModel.playerProgress(),
                onProgressChange = {
                    viewModel.seekProgress(it)
                }
            )
        }
    }
}

@Composable
private fun AnalysisGuidePanel(){
    Text(
        text = stringResource(R.string.analysis_guide),
        color = Color.White,
        fontSize = 18.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )

    Spacer(modifier = Modifier.height(16.dp))

    Box(
        modifier = Modifier.size(250.dp)
    ){
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.analysis_guide),
            contentDescription = null,
            alpha = 0.7f
        )

        Text(
            text = stringResource(R.string.step_one_recording),
            color = Color.White,
            modifier = Modifier.align(Alignment.TopCenter).offset(x = 35.dp, y = 30.dp)
        )

        Text(
            text = stringResource(R.string.step_two_transcription),
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterStart).offset(x = 20.dp, y = 10.dp)
        )

        Text(
            text = stringResource(R.string.step_three_analysis),
            color = Color.White,
            modifier = Modifier.align(Alignment.BottomCenter).offset(x = 30.dp, y = (-20).dp)
        )
    }

    Spacer(modifier = Modifier.height(16.dp))
}

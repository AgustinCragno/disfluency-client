package com.disfluency.screens.patient.exercises

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
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
import com.disfluency.components.button.RecordButton
import com.disfluency.model.exercise.Exercise
import com.disfluency.model.exercise.ExerciseAssignment
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.BackNavigationScaffold
import com.disfluency.ui.theme.DisfluencyTheme
import com.disfluency.viewmodel.ExercisesViewModel
import com.disfluency.viewmodel.record.RecordAudioViewModel
import com.disfluency.viewmodel.record.RecordExerciseAssignmentViewModel
import com.disfluency.viewmodel.states.ConfirmationState
import java.time.LocalDate

private const val BOTTOM_SHEET_PEEK_HEIGHT = 80

private const val BOTTOM_SHEET_TITLE_PADDING_OPEN = 46f
private const val BOTTOM_SHEET_TITLE_PADDING_CLOSED = 16f
private const val BOTTOM_SHEET_REQUIRED_HEIGHT = 200


@Preview
@Composable
fun RecordExercisePreview(){
    val exercisesViewModel = ExercisesViewModel()
    val recordViewModel = RecordExerciseAssignmentViewModel(LocalContext.current, LocalLifecycleOwner.current)

    val navHostController = rememberNavController()

    val assignmentId = "Id"

    val exercise = Exercise(
        "12345678",
        "Velocidad cómoda y fluida",
//        "Hola",
        "Controlar la velocidad de manera que me sea cómodo, tratar de mantenerla ajustándola a mi comodidad. Hablá a una velocidad cómoda y constante a lo largo de las palabras; y de las frases; bajá un poco la velocidad cuando notás un poco de tensión en tu máquina de hablar",
        "La usabilidad es la capacidad del producto software para ser entendido, aprendido, usado y resultar atractivo para el usuario, cuando se usa bajo determinadas condiciones",
        "https://pf5302.s3.us-east-2.amazonaws.com/audios/velocidad.mp3"
    )

    val assignment = ExerciseAssignment(
        id = assignmentId,
        exercise = exercise,
        dateOfAssignment = LocalDate.now(),
        practiceAttempts = mutableListOf()
    )

    exercisesViewModel.assignments.value = listOf(assignment)

    DisfluencyTheme() {
        RecordExerciseScreen(
            assignmentId = assignmentId,
            navController = navHostController,
            exercisesViewModel = exercisesViewModel,
            recordViewModel = recordViewModel
        )
    }
}

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecordExercise(
    assignmentId: String,
    exercise: Exercise,
    navController: NavHostController,
    recordViewModel: RecordExerciseAssignmentViewModel
){
    val context = LocalContext.current
    val isMenuExtended = remember { mutableStateOf(false) }

    val scaffoldState = rememberBottomSheetScaffoldState()

    var animateButtonScale = remember{ derivedStateOf { 1f } }
    var animateTitlePadding = remember {
        derivedStateOf { BOTTOM_SHEET_TITLE_PADDING_OPEN }
    }

    BackNavigationScaffold(title = stringResource(R.string.practice), navController = navController) { paddingValues ->
        Box(
            Modifier.fillMaxSize()
        ) {
            BottomSheetScaffold(
                sheetContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(horizontal = 32.dp)
                    ){
                        ExerciseInstructionsPanel(
                            exercise = exercise,
                            animatePadding = animateTitlePadding
                        )
                    }
                },
                sheetContainerColor = MaterialTheme.colorScheme.secondary,
                containerColor = Color.White,
                sheetPeekHeight = BOTTOM_SHEET_PEEK_HEIGHT.dp,
                sheetDragHandle = {},
                scaffoldState = scaffoldState,
                sheetSwipeEnabled = !isMenuExtended.value
            ) { bottomSheetPaddingValues ->

                ExercisePhrasePanel(
                    exercise = exercise,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottomSheetPaddingValues)
                        .padding(paddingValues),
                    viewModel = recordViewModel
                )
            }

            RecordButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = (BOTTOM_SHEET_PEEK_HEIGHT.dp + 16.dp) / 2)
                    .scale(animateButtonScale.value),
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
                    recordViewModel.uploadRecording(assignmentId)
                },
                onPlay = {
                    recordViewModel.play()
                }
            )
        }
    }


    val initialMeasure: MutableState<Float?> = remember { mutableStateOf(null) }
    LaunchedEffect(Unit){
        initialMeasure.value = scaffoldState.bottomSheetState.requireOffset() - 1f
    }

    initialMeasure.value?.let {
        animateButtonScale = animateFloatAsState(
            targetValue = if (scaffoldState.bottomSheetState.requireOffset() >= it)
                1f else 0f,
            animationSpec = tween(100)
        )

        animateTitlePadding = animateFloatAsState(
            targetValue = if (scaffoldState.bottomSheetState.requireOffset() >= it * 0.7f)
                BOTTOM_SHEET_TITLE_PADDING_OPEN else BOTTOM_SHEET_TITLE_PADDING_CLOSED
        )
    }
}

@Composable
private fun ExerciseInstructionsPanel(
    exercise: Exercise,
    animatePadding: State<Float>
){
    Column(
        modifier = Modifier
            .heightIn(BOTTOM_SHEET_REQUIRED_HEIGHT.dp)
            .padding(top = animatePadding.value.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = exercise.title,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

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


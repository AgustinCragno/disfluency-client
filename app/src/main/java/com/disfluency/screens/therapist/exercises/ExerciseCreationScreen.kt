package com.disfluency.screens.therapist.exercises

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.audio.AudioMediaType
import com.disfluency.components.audio.AudioPlayer
import com.disfluency.components.button.RecordButtonSmall
import com.disfluency.components.inputs.text.CleanLabeledTextField
import com.disfluency.components.stepper.PageStepper
import com.disfluency.components.stepper.StepScreen
import com.disfluency.model.exercise.Exercise
import com.disfluency.model.user.Therapist
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.BackNavigationScaffold
import com.disfluency.screens.patient.exercises.ExercisePhrasePanel
import com.disfluency.viewmodel.ExercisesViewModel
import com.disfluency.viewmodel.record.LOCAL_RECORD_FILE
import com.disfluency.viewmodel.record.RecordAudioViewModel
import com.disfluency.viewmodel.record.RecordExerciseExampleViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ExerciseCreationScreen(
    therapist: Therapist,
    navController: NavHostController,
    viewModel: ExercisesViewModel,
    recordViewModel: RecordExerciseExampleViewModel
){
    BackNavigationScaffold(
        title = stringResource(R.string.new_exercise),
        navController = navController
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ExerciseCreationStepper(
                therapist = therapist,
                navController = navController,
                recordViewModel = recordViewModel
            )
        }
    }
}



@Composable
private fun ExerciseCreationStepper(
    therapist: Therapist,
    navController: NavHostController,
    recordViewModel: RecordExerciseExampleViewModel
){
    val title = remember { mutableStateOf("") }
    val instruction = remember { mutableStateOf("") }
    val phrase = remember { mutableStateOf("") }

    val steps = listOf(
        StepScreen(
            stepTitle = stringResource(id = R.string.instruction),
            forwardEnabled = title.value.isNotBlank() && instruction.value.isNotBlank(),
            backButtonText = stringResource(id = R.string.cancel)
        ){
            ExerciseValuesInput(title = title, instruction = instruction, phrase = phrase)
        },
        StepScreen(
            stepTitle = stringResource(id = R.string.example)
        ){
            ExampleGuide()
        },
        StepScreen(
            stepTitle = stringResource(id = R.string.record),
            forwardEnabled = recordViewModel.audioRecorder.hasRecorded.value
        ){
            ExampleRecording(title = title, instruction = instruction, phrase = phrase, viewModel = recordViewModel)
        },
        StepScreen(
            stepTitle = stringResource(id = R.string.confirm),
            nextButtonText = stringResource(id = R.string.confirm)
        ){
            ExerciseOverview(title = title, instruction = instruction, phrase = phrase, audioUrl = LOCAL_RECORD_FILE)
        }
    )

    var submitted by remember { mutableStateOf(false) }

    PageStepper(
        steps = steps,
        onCancel = { navController.popBackStack() }
    ) {
        recordViewModel.uploadRecording(therapist.id, title.value, instruction.value, phrase.value.ifBlank { null })
        CoroutineScope(Dispatchers.IO).launch {
            delay(200)
            submitted = true
        }
    }

    if (submitted){
        LaunchedEffect(Unit){
            navController.popBackStack()
            navController.navigate(Route.Therapist.ConfirmationNewExercise.path)
        }
    }
}


@Composable
private fun ExerciseValuesInput(
    title: MutableState<String>,
    instruction: MutableState<String>,
    phrase: MutableState<String>,
    edit: Boolean = true
){
    Column(
        modifier = Modifier
//            .fillMaxSize()
            .padding(horizontal = 32.dp)
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        CleanLabeledTextField(
            input = title,
            label = stringResource(id = R.string.title),
            placeholder = stringResource(R.string.enter_title),
            textStyle = MaterialTheme.typography.displayMedium,
            enabled = edit,
            fontSize = 22.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        CleanLabeledTextField(
            input = instruction,
            label = stringResource(id = R.string.instruction),
            placeholder = stringResource(R.string.enter_instruction),
            enabled = title.value.isNotBlank() && edit,
            fontSize = 16.sp
        )


        if (edit || phrase.value.isNotBlank()){
            Spacer(modifier = Modifier.height(4.dp))

            CleanLabeledTextField(
                input = phrase,
                label = stringResource(id = R.string.phrase_optional),
                placeholder = stringResource(R.string.enter_phrase),
                enabled = title.value.isNotBlank() && instruction.value.isNotBlank() && edit,
                fontSize = 16.sp,
                keyboardAction = ImeAction.Done
            )
        }
    }
}

@Composable
private fun ExampleGuide(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp)
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.example_audio),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.secondary
        )

        val size = 13.sp
        Text(
            text = stringResource(R.string.example_audio_desc),
            fontSize = size,
            lineHeight = size,
            color = Color.DarkGray
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ){
            Image(
                painter = painterResource(id = R.drawable.record_action),
                contentDescription = null,
                modifier = Modifier.size(150.dp),
                alpha = 0.8f
            )
        }

        Text(
            text = stringResource(id = R.string.phrase),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = stringResource(R.string.phrase_desc),
            fontSize = size,
            lineHeight = size,
            color = Color.DarkGray
        )
    }
}

@Composable
private fun ExampleRecording(
    title: MutableState<String>,
    instruction: MutableState<String>,
    phrase: MutableState<String>,
    viewModel: RecordAudioViewModel
){
    val context = LocalContext.current

    val exercise = Exercise(
        id = "",
        title = title.value,
        instruction = instruction.value,
        phrase = phrase.value.ifBlank { null },
        sampleRecordingUrl = ""
    )

    val isMenuExtended = remember { mutableStateOf(viewModel.audioRecorder.hasRecorded.value) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        ExercisePhrasePanel(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            exercise = exercise,
            viewModel = viewModel
        )

        RecordButtonSmall(
            modifier = Modifier.padding(top = 40.dp),
            isMenuExtended = isMenuExtended,
            viewModel = viewModel,
            onPress = {
                viewModel.start(context)
            },
            onRelease = {
                viewModel.stop()
            },
            onDelete = {
                viewModel.delete()
            },
            onPlay = {
                viewModel.play()
            }
        )

    }
}

@Composable
private fun ExerciseOverview(
    title: MutableState<String>,
    instruction: MutableState<String>,
    phrase: MutableState<String>,
    audioUrl: String
){
    Column {
        ExerciseValuesInput(title = title, instruction = instruction, phrase = phrase, edit = false)

        AudioPlayer(
            url = audioUrl,
            type = AudioMediaType.FILE,
            playButtonColor = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(32.dp)
        )
    }
}


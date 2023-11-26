package com.disfluency.screens.patient.exercises

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.QueryStats
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.disfluency.R
import com.disfluency.components.audio.AudioMediaType
import com.disfluency.components.audio.AudioPlayer
import com.disfluency.components.icon.IconLabeled
import com.disfluency.components.icon.ImageMessagePage
import com.disfluency.components.skeleton.FullScreenLoader
import com.disfluency.model.analysis.Analysis
import com.disfluency.model.exercise.Exercise
import com.disfluency.model.exercise.ExerciseAssignment
import com.disfluency.model.exercise.ExercisePractice
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.BackNavigationScaffold
import com.disfluency.screens.therapist.exercises.ExerciseDetailPanel
import com.disfluency.ui.theme.DisfluencyTheme
import com.disfluency.utilities.format.formatLocalDateAsWords
import com.disfluency.utilities.format.formatLocalTime
import com.disfluency.viewmodel.ExercisesViewModel
import java.time.LocalDate
import java.time.LocalDateTime


@Composable
fun ExerciseAssignmentDetailScreen(patientId: String, assignmentId: String, navController: NavHostController, viewModel: ExercisesViewModel){
    LaunchedEffect(Unit){
        //viewModel.getAssignmentsOfPatient(patientId) //TODO revisar tuvimos que comentar esta linea para que no se ejecuten dos llamados al back en esta pantalla
    }

    BackNavigationScaffold(title = stringResource(R.string.exercises), navController = navController) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            viewModel.getAssignmentById(assignmentId)?.let {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    ExerciseDetailPanel(exercise = it.exercise)

                    ExercisePracticeList(
                        assignment = it,
                        title = stringResource(R.string.my_practices),
                        emptyListContent = { NoPracticesMessage() },
                        navController = navController
                    )

                    Spacer(modifier = Modifier.height(64.dp))
                }

                PracticeButton(assignmentId = assignmentId, navController = navController)
            }
                ?:
            FullScreenLoader()
        }
    }
}

@Composable
fun ExercisePracticeList(
    assignment: ExerciseAssignment,
    title: String,
    emptyListContent: @Composable () -> Unit,
    navController: NavHostController,
    analysisEnabled: Boolean = false
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 8.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = title,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier
                    .background(Color.LightGray.copy(alpha = 0.15f))
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            Divider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 3.dp,
                color = Color.Gray.copy(alpha = 0.3f)
            )

            if (assignment.practiceAttempts.isNotEmpty()){
                assignment.practiceAttempts.forEachIndexed { index, practice ->
                    if (index != 0) Divider()
                    ExercisePracticeItem(
                        practice = practice,
                        index = index,
                        navController = navController,
                        analysisEnabled = analysisEnabled
                    )
                }
            } else{
                emptyListContent()
            }
        }
    }
}

@Composable
fun ExercisePracticeItem(practice: ExercisePractice, index: Int, navController: NavHostController, analysisEnabled: Boolean = false){
    var expanded by remember {
        mutableStateOf(false)
    }

    //TODO: el problema con esto es que Amplituda choca cuando va a buscarlo al mismo tiempo,
    // de ultima por el momento lo podemos dejar como antes y despues lo vemos.
    //
//    val audioPlayer = DisfluencyAudioUrlPlayer(LocalContext.current)
//
//    LaunchedEffect(Unit){
//        audioPlayer.load(practice.recordingUrl)
//    }
//
//    DisposableEffect(Lifecycle.Event.ON_STOP) {
//        onDispose {
//            audioPlayer.release()
//        }
//    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
            )
            .clickable { expanded = expanded.not() }
    ){
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "#${index + 1}",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = formatLocalDateAsWords(practice.date.toLocalDate(), stringResource(id = R.string.locale))
                )

                Spacer(modifier = Modifier.weight(1f))

                IconLabeled(
                    icon = Icons.Outlined.AccessTime,
                    label = formatLocalTime(practice.date.toLocalTime()),
                    labelColor = Color.Gray
                )

                if (analysisEnabled){
                    ViewAnalysisAction(exercisePracticeId = practice.id, navController = navController)
                }

            }

            AnimatedVisibility(
                visible = expanded,
                exit = fadeOut(tween(200)) + shrinkVertically(tween(400))
            ) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))

//                AudioPlayer(audioPlayer = audioPlayer)
                    AudioPlayer(url = practice.recordingUrl, AudioMediaType.URL)

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun ViewAnalysisAction(exercisePracticeId: String, navController: NavHostController){
    IconButton(
        onClick = {
            navController.navigate(Route.Therapist.ExerciseAnalysisTranscription.routeTo(exercisePracticeId))
        },
    )
    {
        Icon(
            imageVector = Icons.Outlined.QueryStats,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview
@Composable
private fun AssignmentDetailPreview(){
    val exercisesViewModel = ExercisesViewModel()
    val navHostController = rememberNavController()

    val assignmentId = "Id"

    val exercise = Exercise(
        "12345678",
        "Velocidad cómoda y fluida",
        "Controlar la velocidad de manera que me sea cómodo, tratar de mantenerla ajustándola a mi comodidad. Hablá a una velocidad cómoda y constante a lo largo de las palabras; y de las frases; bajá un poco la velocidad cuando notás un poco de tensión en tu máquina de hablar",
        "La usabilidad es la capacidad del producto software para ser entendido, aprendido, usado y resultar atractivo para el usuario, cuando se usa bajo determinadas condiciones",
        "https://pf5302.s3.us-east-2.amazonaws.com/audios/velocidad.mp3"
    )

    val practice1 = ExercisePractice(
        "1",
        LocalDateTime.now(),
        "https://pf5302.s3.us-east-2.amazonaws.com/audios/iniciosuave.mp3",
        null
    )

    val practice2 = ExercisePractice(
        "1",
        LocalDateTime.now(),
        "https://pf5302.s3.us-east-2.amazonaws.com/audios/toquesligeros.mp3",
        null
    )

    val practice3 = ExercisePractice(
        "1",
        LocalDateTime.now(),
        "https://pf5302.s3.us-east-2.amazonaws.com/audios/fonacion.mp3",
        null
    )

    val assignment = ExerciseAssignment(
        id = assignmentId,
        exercise = exercise,
        dateOfAssignment = LocalDate.now(),
        practiceAttempts = mutableListOf(practice1, practice2, practice3),
//        practiceAttempts = mutableListOf()
    )

    exercisesViewModel.assignments.value = listOf(assignment)

    DisfluencyTheme{

        ExercisePracticeItem(practice2, 1, navHostController);
    }
}


@Composable
private fun NoPracticesMessage(){
    ImageMessagePage(
        imageResource = R.drawable.record_action,
        imageSize = 80.dp,
        text = stringResource(R.string.you_havent_practiced_this_exercise)
    )
}

@Composable
private fun PracticeButton(assignmentId: String, navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(
            onClick = {
                navController.navigate(Route.Patient.ExercisePractice.routeTo(assignmentId))
            },
            modifier = Modifier.padding(16.dp),
            containerColor = MaterialTheme.colorScheme.secondary
        ) {
            Icon(Icons.Filled.Mic, stringResource(id = R.string.practice))
        }
    }
}

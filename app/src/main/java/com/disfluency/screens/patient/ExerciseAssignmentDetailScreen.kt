package com.disfluency.screens.patient

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.outlined.AccessTime
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
import com.disfluency.model.Exercise
import com.disfluency.model.ExerciseAssignment
import com.disfluency.model.ExercisePractice
import com.disfluency.navigation.routing.Route
import com.disfluency.screens.login.BackNavigationScaffold
import com.disfluency.screens.therapist.ExerciseDetailScreen
import com.disfluency.ui.theme.DisfluencyTheme
import com.disfluency.utilities.format.formatLocalDateAsWords
import com.disfluency.viewmodel.ExercisesViewModel
import java.time.LocalDate

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
        LocalDate.now(),
        "https://pf5302.s3.us-east-2.amazonaws.com/audios/velocidad.mp3"
    )

    val practice2 = ExercisePractice(
        "1",
        LocalDate.now(),
        "https://pf5302.s3.us-east-2.amazonaws.com/audios/velocidad.mp3"
    )

    val practice3 = ExercisePractice(
        "1",
        LocalDate.now(),
        "https://pf5302.s3.us-east-2.amazonaws.com/audios/velocidad.mp3"
    )

    val assignment = ExerciseAssignment(
        id = assignmentId,
        exercise = exercise,
        dateOfAssignment = LocalDate.now(),
        practiceAttempts = mutableListOf(practice1, practice2, practice3),
//        practiceAttempts = mutableListOf()
    )

    exercisesViewModel.assignments.value = listOf(assignment)

    DisfluencyTheme() {
        BackNavigationScaffold(title = R.string.exercises, navController = navHostController) { paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
            ){
                ExerciseAssignmentDetailScreen(
                    assignmentId = assignmentId,
                    navController = navHostController,
                    viewModel = exercisesViewModel
                )
            }
        }
    }
}

@Composable
fun ExerciseAssignmentDetailScreen(assignmentId: String, navController: NavHostController, viewModel: ExercisesViewModel){
    val assignment = remember { mutableStateOf<ExerciseAssignment?>(null) }

    LaunchedEffect(Unit){
        assignment.value = viewModel.getAssignmentById(assignmentId)
    }

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        assignment.value?.let {
            ExerciseDetailScreen(exercise = it.exercise)

            ExercisePracticeList(assignment = it)
        }
    }

    PracticeButton(assignmentId = assignmentId, navController = navController)
}

@Composable
private fun ExercisePracticeList(assignment: ExerciseAssignment){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Mis resoluciones",
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
                    ExercisePracticeItem(practice = practice, index = index)
                }
            } else{
                //TODO: mostrar algun texto que diga que no hay resoluciones
                NoPracticesMessage()
            }
        }
    }

    Spacer(modifier = Modifier.height(64.dp))
}

@Composable
private fun ExercisePracticeItem(practice: ExercisePractice, index: Int){
    var expanded by remember {
        mutableStateOf(false)
    }

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

                //TODO: podriamos cambiar el modelo para que guarde LocalDateTime, asi queda el horario y se puede
                // diferenciar varias resoluciones del mismo dia
                Text(
                    text = formatLocalDateAsWords(practice.date, stringResource(id = R.string.locale))
                )

                Spacer(modifier = Modifier.weight(1f))

                IconLabeled(
                    icon = Icons.Outlined.AccessTime,
                    label = "17:53",
                    labelColor = Color.Gray
                )
            }

            if (expanded){
                Spacer(modifier = Modifier.height(16.dp))

                AudioPlayer(url = practice.recordingUrl, type = AudioMediaType.URL)

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun NoPracticesMessage(){
    ImageMessagePage(imageResource = R.drawable.record_action, text = stringResource(R.string.you_havent_practiced_this_exercise))
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
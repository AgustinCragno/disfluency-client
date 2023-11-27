package com.disfluency.screens.patient.home

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.RecordVoiceOver
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.disfluency.components.bar.HomeTopAppBar
import com.disfluency.components.list.item.ListItem
import com.disfluency.components.thumbnail.TitleThumbnail
import com.disfluency.model.exercise.Exercise
import com.disfluency.model.exercise.ExerciseAssignment
import com.disfluency.model.exercise.ExercisePractice
import com.disfluency.model.form.Form
import com.disfluency.model.form.FormAssignment
import com.disfluency.model.form.FormCompletionEntry
import com.disfluency.model.user.Patient
import com.disfluency.navigation.routing.BottomNavigationItem
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.BottomNavigationScaffold
import com.disfluency.screens.patient.home.carousel.LastTwoWeeksRecapPanel
import com.disfluency.screens.patient.home.carousel.PatientWelcomePanel
import com.disfluency.screens.patient.home.carousel.PendingAssignmentsPanel
import com.disfluency.screens.patient.home.chart.WeeklyProgressChart
import com.disfluency.ui.theme.DisfluencyTheme
import com.disfluency.utilities.color.darken
import com.disfluency.utilities.color.stringToRGB
import com.disfluency.utilities.format.formatLocalDate
import com.disfluency.viewmodel.LoggedUserViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import kotlin.math.absoluteValue

@Preview
@Composable
private fun HomePatientPreview(){

    val assignForm: (String, LocalDate, Int?) -> FormAssignment = { title, date, resolutionsCount ->
        val form = Form(
            id = "1",
            title = title,
            questions = emptyList()
        )

        FormAssignment(
            id = "1",
            form = form,
            date = date,
            completionEntries = resolutionsCount?.let {
                (0 until resolutionsCount).map {
                    FormCompletionEntry(it.toString(), date.plusDays(it.toLong()), emptyList())
                }
            } ?: emptyList()
        )
    }

    val assignExercise: (String, LocalDate, Int?) -> ExerciseAssignment = { title, date, resolutionsCount ->
        val exercise = Exercise(
            id = "1",
            title = title,
            instruction = "",
            phrase = "",
            sampleRecordingUrl = ""
        )

        ExerciseAssignment(
            id = "1",
            exercise = exercise,
            dateOfAssignment = date,
            practiceAttempts = resolutionsCount?.let {
                (0 until resolutionsCount).map {
                    ExercisePractice(it.toString(), date.plusDays(it.toLong()).atStartOfDay(), "")
                }.toMutableList()
            } ?: mutableListOf()
        )
    }





    val patient = Patient(
        id = "",
        name = "",
        lastName = "",
        dateOfBirth = LocalDate.now(),
        email = "",
        joinedSince = LocalDate.now().minusWeeks(6),
        avatarIndex = 1,
        weeklyTurn = listOf(DayOfWeek.MONDAY),
        weeklyHour = LocalTime.now(),
        exercises = listOf(
            assignExercise("Fonacion Continuada", LocalDate.now().minusWeeks(3), null),
            assignExercise("Toque Ligero", LocalDate.now().minusWeeks(5), 3),
            assignExercise("Inicio Suave", LocalDate.now().minusWeeks(4), 7)
        ),
        forms = listOf(
            assignForm("Situaciones en el colegio", LocalDate.now().minusDays(1), null),
            assignForm("Situaciones sociales", LocalDate.now().minusDays(8), 2),
            assignForm("Cuestionario SSI 3", LocalDate.now().minusDays(12), null)
        )
    )

    val navController = rememberNavController()

    val viewModel = LoggedUserViewModel(navController)

    DisfluencyTheme() {
        HomePatientScreen(patient = patient, navController = navController, viewModel = viewModel)
    }
}

@Composable
fun HomePatientScreen(patient: Patient, navController: NavHostController, viewModel: LoggedUserViewModel){
    BottomNavigationScaffold(
        bottomNavigationItems = BottomNavigationItem.Patient.items(),
        navController = navController
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            HomeTopAppBar(navController = navController, viewModel = viewModel)

            HomeScreenContent(patient = patient, navController = navController)
        }
    }
}

@Composable
private fun HomeScreenContent(patient: Patient, navController: NavHostController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        WelcomeBackCarousel(patient = patient)

        Spacer(modifier = Modifier.height(24.dp))

        News(patient = patient, navController = navController)

        Spacer(modifier = Modifier.height(24.dp))

        Pending(patient = patient, navController = navController)

        Spacer(modifier = Modifier.height(24.dp))

        Progress(patient = patient)

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun WelcomeBackCarousel(patient: Patient){

    val pagerState = rememberPagerState()
    val autoScrollDuration = 5000L

    val items = listOf<@Composable () -> Unit>(
        { PatientWelcomePanel() },
        { PendingAssignmentsPanel(patient.progressInfo!!) },
        { LastTwoWeeksRecapPanel(patient.progressInfo!!) }
    )

    val pageCount = items.size

    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
    if (isDragged.not()) {
        with(pagerState) {
            var currentPageKey by remember { mutableStateOf(0) }
            LaunchedEffect(key1 = currentPageKey) {
                launch {
                    delay(timeMillis = autoScrollDuration)
                    val nextPage = (currentPage + 1).mod(pageCount)
                    animateScrollToPage(page = nextPage)
                    currentPageKey = nextPage
                }
            }
        }
    }

    Box {
        HorizontalPager(count = pageCount, state = pagerState) { page ->
            items[page].invoke()
        }

        DotIndicators(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            pageCount = pageCount,
            pagerState = pagerState
        )
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun DotIndicators(
    pageCount: Int,
    pagerState: PagerState,
    selectedColor: Color = Color.White.copy(alpha = 0.5f),
    unselectedColor: Color = Color.DarkGray.copy(alpha = 0.5f),
    modifier: Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        repeat(pageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration) selectedColor else unselectedColor
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
fun Modifier.carouselTransition(page: Int, pagerState: PagerState) =
    graphicsLayer {
        val pageOffset =
            ((pagerState.currentPage - page) + pagerState.currentPageOffset).absoluteValue

        val transformation =
            lerp(
                start = 0.7f,
                stop = 1f,
                fraction = 1f - pageOffset.coerceIn(0f, 1f)
            )
        alpha = transformation
        scaleY = transformation
    }

fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return start * (1 - fraction) + stop * fraction
}

@Composable
private fun News(
    patient: Patient,
    navController: NavHostController
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "Novedades",
            style = MaterialTheme.typography.displayMedium,
            color = Color.Black,
            fontSize = 24.sp
        )

        Text(
            text = "Ultimos materiales asignados por tu terapeuta",
            color = Color.Gray,
            fontSize = 13.sp,
            lineHeight = 14.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        val lastAssignedExercise = patient.progressInfo!!.lastAssignedExercise
        val lastAssignedForm = patient.progressInfo.lastAssignedForm

        lastAssignedExercise?.let {
            PendingExerciseListItem(
                exerciseAssignment = it,
                navController = navController
            )
        }

        lastAssignedForm?.let {
            Spacer(modifier = Modifier.height(8.dp))

            PendingFormListItem(
                formAssignment = it,
                navController = navController
            )
        }

        if (lastAssignedExercise == null && lastAssignedForm == null){
            DefaultMessageCard(title = "Por el momento no tenes material asignado")
        }
    }

}


@Composable
private fun Pending(
    patient: Patient,
    navController: NavHostController
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "Pendientes",
            style = MaterialTheme.typography.displayMedium,
            color = Color.Black,
            fontSize = 24.sp
        )

        val pendingExercises = patient.progressInfo!!.pendingExercises.toMutableList()
        val pendingForms = patient.progressInfo.pendingForms.toMutableList()

        patient.progressInfo.lastAssignedExercise?.let {
            if (pendingExercises.map { e -> e.id }.contains(it.id)){
                pendingExercises.removeIf { e -> e.id == it.id }
            }
        }

        patient.progressInfo.lastAssignedForm?.let {
            if (pendingForms.map { f -> f.id }.contains(it.id)){
                pendingForms.removeIf { f -> f.id == it.id }
            }
        }

        if (pendingExercises.isEmpty() && pendingForms.isEmpty()){
            Spacer(modifier = Modifier.height(8.dp))
            DefaultMessageCard(title = "¡Buen trabajo! No tenes tareas pendientes al dia de hoy")
        } else {
            PendingList(pendingExercises = pendingExercises, pendingForms = pendingForms, navController = navController)
        }
    }
}

@Composable
private fun PendingList(
    pendingExercises: List<ExerciseAssignment>,
    pendingForms: List<FormAssignment>,
    navController: NavHostController
){
    if (pendingExercises.isNotEmpty()) {
        Text(
            text = "Ejercicios foneticos sin resolver",
            color = Color.Gray,
            fontSize = 13.sp,
            lineHeight = 14.sp
        )
    }

    pendingExercises.forEach {
        Spacer(modifier = Modifier.height(8.dp))

        PendingExerciseListItem(
            exerciseAssignment = it,
            navController = navController
        )
    }

    if (pendingExercises.isNotEmpty()) Spacer(modifier = Modifier.height(16.dp))

    if (pendingForms.isNotEmpty()){
        Text(
            text = "Cuestionarios de autopercepcion sin resolver",
            color = Color.Gray,
            fontSize = 13.sp,
            lineHeight = 14.sp
        )
    }

    pendingForms.forEach {
        Spacer(modifier = Modifier.height(8.dp))

        PendingFormListItem(
            formAssignment = it,
            navController = navController
        )
    }
}

@Composable
fun PendingFormListItem(
    formAssignment: FormAssignment,
    navController: NavHostController
){
    ListItem(
        title = formAssignment.form.title,
        subtitle = formatLocalDate(formAssignment.date),
        leadingContent = {
            TitleThumbnail(formAssignment.form.title)
        },
        trailingContent = {
            Icon(
                imageVector = Icons.Outlined.Assignment,
                contentDescription = null,
                modifier = Modifier.size(17.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        },
        onClick = {
            navController.navigate(Route.Patient.FormCompletion.routeTo(formAssignment.id))
        }
    )
}


@Composable
fun PendingExerciseListItem(exerciseAssignment: ExerciseAssignment, navController: NavHostController){

    ListItem(
        title = exerciseAssignment.exercise.title,
        subtitle = formatLocalDate(exerciseAssignment.dateOfAssignment),
        leadingContent = {
            TitleThumbnail(exerciseAssignment.exercise.title)
        },
        trailingContent = {
            Icon(
                imageVector = Icons.Outlined.RecordVoiceOver,
                contentDescription = null,
                modifier = Modifier.size(17.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        },
        onClick = {
            navController.navigate(
                Route.Patient.ExerciseAssignmentDetail.routeTo(exerciseAssignment.id)
            )
        }
    )
}

@Composable
private fun Progress(patient: Patient){
    val data = patient.progressInfo!!.weeklyProgressMap

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "Progreso",
            style = MaterialTheme.typography.displayMedium,
            color = Color.Black,
            fontSize = 24.sp
        )

        Text(
            text = "Visualiza la cantidad de practicas realizadas por semana desde que comenzo tu viaje con Disfluency",
            color = Color.Gray,
            fontSize = 13.sp,
            lineHeight = 14.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (data.size > 1){
            Card(
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                ProgressChart(data = data)
            }
        } else {
            DefaultMessageCard(title = "¡Bienvenido! A partir de la proxima semana podras ver el progreso de tus prácticas")
        }
    }
}

@Composable
private fun ProgressChart(data: Map<LocalDate, Int>){
    Box(modifier = Modifier.fillMaxWidth()){
        Text(
            text = "Cantidad de ejercicios resueltos y cuestionarios repondidos por semana",
            fontSize = 14.sp,
            lineHeight = 15.sp,
            color = MaterialTheme.colorScheme.secondary.darken(),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        WeeklyProgressChart(weeklyProgress = data)
    }
}

@Composable
private fun DefaultMessageCard(title: String){
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            fontSize = 13.sp,
            lineHeight = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        )
    }

}
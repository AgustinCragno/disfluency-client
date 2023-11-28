package com.disfluency.screens.therapist.patients

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.api.error.PatientNotFoundException
import com.disfluency.components.grid.span.TwoColumnGridItemSpan
import com.disfluency.components.icon.IconLabeled
import com.disfluency.model.user.Patient
import com.disfluency.model.user.Therapist
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.BackNavigationScaffold
import com.disfluency.utilities.color.darken
import com.disfluency.utilities.format.formatWeeklyTurn
import com.disfluency.viewmodel.PatientsViewModel
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@Composable
fun PatientDetailScreen(therapist: Therapist, patientId: String, navController: NavHostController, viewModel: PatientsViewModel){

    LaunchedEffect(Unit) {
        viewModel.getPatientsByTherapist(therapist.id)
    }

    BackNavigationScaffold(
        title = stringResource(R.string.patient),
        navController = navController
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            viewModel.getPatientById(patientId)?.let {
                PatientInfoCard(patient = it)
                ButtonPanel(patient = it, navController = navController)
                ActivitiesOverview(patient = it)
            }
                ?:
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                CircularProgressIndicator()
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PatientInfoCard(patient: Patient){
    Card(
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .height(122.dp)
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = patient.avatar()),
                contentDescription = "User Thumbnail",
                modifier = Modifier.size(90.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = patient.fullName(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(4.dp))

                FlowRow {
                    IconLabeled(
                        icon = Icons.Outlined.CalendarMonth,
                        label = formatWeeklyTurn(patient.weeklyTurn)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconLabeled(
                        icon = Icons.Outlined.AccessTime,
                        label = patient.weeklyHour.format(
                            DateTimeFormatter.ofPattern(stringResource(
                            R.string.time_format)))
                    )
                }
            }
        }
    }
}

@Composable
fun ButtonPanel(patient: Patient, navController: NavController){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ActivityButton(title = stringResource(R.string.exercises), icon = Icons.Outlined.RecordVoiceOver, onClick = { navController.navigate(Route.Therapist.PatientExercises.routeTo(patient.id)) })
        ActivityButton(title = stringResource(R.string.forms), icon = Icons.Outlined.Assignment, onClick = { navController.navigate(Route.Therapist.PatientForms.routeTo(patient.id)) })
        ActivityButton(title = stringResource(R.string.sessions), icon = Icons.Outlined.Mic, onClick = { navController.navigate(Route.Therapist.PatientSessions.routeTo(patient.id)) })
    }
}

@Composable
fun ActivityButton(title: String, icon: ImageVector, onClick: () -> Unit){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier.size(42.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            contentPadding = PaddingValues(1.dp)
        ) {
            Box {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            fontWeight = FontWeight.SemiBold
        )
    }

}

@Composable
fun ActivitiesOverview(patient: Patient){

    data class ActivityOverviewItem(val title: String, val number: Int, val color: Color)

    val activities = listOf(
        ActivityOverviewItem(stringResource(R.string.solved_exercises), patient.getCompletedExercisesCount(), MaterialTheme.colorScheme.primary),
        ActivityOverviewItem(stringResource(R.string.pending_exercises), patient.getPendingExercisesCount(), MaterialTheme.colorScheme.primary.darken(0.2f)),
        ActivityOverviewItem(stringResource(R.string.solved_forms), patient.getCompletedQuestionnairesCount(), Color.Red),
        ActivityOverviewItem(stringResource(R.string.pending_forms), patient.getPendingQuestionnairesCount(), Color.Red.darken(0.2f)),
        ActivityOverviewItem(stringResource(R.string.recorded_sessions), patient.getRecordedSessionsCount(), Color.Blue.darken(0.2f))
    )

    //TODO: lo ideal aca seria que no sea scrolleable, deberiamos ver una forma que cuando no haya espacio para meter todo,
    //achiquemos la altura de cada uno de estos paneles, o el padding a lo sumo
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .padding(16.dp)
            .height(250.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        activities.forEachIndexed { index, activity ->
            item(span = { TwoColumnGridItemSpan(activities.size).adjust(index) }) {
                ActivityOverviewCard(title = activity.title, number = activity.number, color = activity.color)
            }
        }
    }
}

@Composable
fun ActivityOverviewCard(title: String, number: Int, color: Color = MaterialTheme.colorScheme.primary){
    Card(
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        modifier = Modifier
            .height(72.dp)
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape),
                color = color
            ) {
                Box(contentAlignment = Alignment.Center){
                    Text(
                        text = number.toString(),
                        style = TextStyle(color = Color.White, fontSize = 18.sp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxHeight()
                    .wrapContentHeight(),
                maxLines = 2,
                lineHeight = 20.sp,
                fontSize = 13.sp, //TODO: se podra hacer que se ajuste al espacio disponible?
                color = Color.Black
            )
        }
    }
}


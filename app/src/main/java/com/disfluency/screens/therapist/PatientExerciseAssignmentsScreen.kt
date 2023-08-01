package com.disfluency.screens.therapist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.icon.ImageMessagePage
import com.disfluency.components.skeleton.SkeletonLoader
import com.disfluency.components.skeleton.list.ExerciseAssignmentListSkeleton
import com.disfluency.components.thumbnail.ExerciseThumbnail
import com.disfluency.model.ExerciseAssignment
import com.disfluency.navigation.routing.Route
import com.disfluency.utilities.format.formatLocalDate
import com.disfluency.viewmodel.ExercisesViewModel

@Composable
fun PatientExerciseAssignmentsScreen(patientId: String, navController: NavHostController, viewModel: ExercisesViewModel = viewModel()) {
    LaunchedEffect(Unit) {
        viewModel.getAssignmentsOfPatient(patientId)
    }

    //TODO: definir como manejar el tema de ir a buscar cosas constantemente.
    // por ejemplo, como terapeuta, mis pacientes solo pueden cambiar cuando yo cargo uno a traves de la aplicacion
    // por lo que una vez que busque todos mis pacientes, no tiene sentido ir a buscarlos de nuevo (por ej en la pantalla de listado)
    // aca por ejemplo pasa lo mismo con las asignaciones, pero por ejemplo para las resoluciones, si deberia ir a actualizarlo siempre
    // ya que puede darse que el paciente resuelva un ejercicio mientras yo estoy en la aplicacion tambien.


    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        SkeletonLoader(
            state = viewModel.assignments,
            content = {
                viewModel.assignments.value?.let {
                    ExerciseAssignmentList(exerciseAssignments = it, navController = navController, onClickRoute = Route.Therapist.ExerciseAssignmentDetail)
                }
            },
            skeleton = {
                ExerciseAssignmentListSkeleton()
            }
        )
    }
}

@Composable
fun ExerciseAssignmentList(exerciseAssignments: List<ExerciseAssignment>, navController: NavHostController, onClickRoute: Route){
    if (exerciseAssignments.isEmpty()){
        NoAssignmentsMessage()
    }

    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(exerciseAssignments) {ex ->
            ExerciseAssignmentListItem(exerciseAssignment = ex, navController = navController, onClickRoute = onClickRoute)
        }
    }
}

@Composable
fun ExerciseAssignmentListItem(exerciseAssignment: ExerciseAssignment, navController: NavHostController, onClickRoute: Route){
    val onClick = {
        navController.navigate(
            onClickRoute.routeTo(exerciseAssignment.id)
        )
    }

    Card(
        modifier = Modifier.clickable { onClick() },
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
    ) {
        ListItem(
            modifier = Modifier.height(56.dp),
            headlineContent = {
                Text(
                    text = exerciseAssignment.exercise.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            supportingContent = {
                Text(
                    text = formatLocalDate(exerciseAssignment.dateOfAssignment),
                    style = MaterialTheme.typography.labelMedium
                )
            },
            leadingContent = {
                ExerciseThumbnail(exercise = exerciseAssignment.exercise)
            },
            trailingContent = {
                Text(
                    text = "${exerciseAssignment.attemptsCount()} ${stringResource(R.string.resolutions)}"
                )
            }
        )
    }
}

@Composable
private fun NoAssignmentsMessage(){
    ImageMessagePage(imageResource = R.drawable.speech_bubble, text = stringResource(id = R.string.patient_has_no_assigned_exercises))
}
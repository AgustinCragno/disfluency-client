package com.disfluency.screens.therapist.exercises

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.icon.IconLabeled
import com.disfluency.components.icon.ImageMessagePage
import com.disfluency.components.list.item.ListItem
import com.disfluency.components.skeleton.SkeletonLoader
import com.disfluency.components.skeleton.list.ExerciseAssignmentListSkeleton
import com.disfluency.components.thumbnail.ExerciseThumbnail
import com.disfluency.model.exercise.ExerciseAssignment
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.BackNavigationScaffold
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


    BackNavigationScaffold(
        title = stringResource(R.string.patient_exercises),
        navController = navController
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SkeletonLoader(
                state = viewModel.assignments,
                content = {
                    viewModel.assignments.value?.let {
                        ExerciseAssignmentList(
                            exerciseAssignments = it,
                            navController = navController,
                            onClickRoute = Route.Therapist.ExerciseAssignmentDetail
                        )
                    }
                },
                skeleton = {
                    ExerciseAssignmentListSkeleton()
                }
            )
        }
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

    ListItem(
        title = exerciseAssignment.exercise.title,
        subtitle = formatLocalDate(exerciseAssignment.dateOfAssignment),
        leadingContent = {
            ExerciseThumbnail(exercise = exerciseAssignment.exercise)
        },
        trailingContent = {
            IconLabeled(
                icon = Icons.Default.Repeat,
                label = exerciseAssignment.attemptsCount().toString(),
                iconColor = MaterialTheme.colorScheme.primary,
                labelColor = MaterialTheme.colorScheme.primary,
                labelSize = 15.sp
            )
        },
        onClick = {
            navController.navigate(
                onClickRoute.routeTo(exerciseAssignment.id)
            )
        }
    )
}

@Composable
private fun NoAssignmentsMessage(){
    ImageMessagePage(imageResource = R.drawable.speech_bubble, text = stringResource(id = R.string.patient_has_no_assigned_exercises))
}
package com.disfluency.screens.therapist.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.icon.ImageMessagePage
import com.disfluency.model.exercise.ExerciseAssignment
import com.disfluency.navigation.structure.BackNavigationScaffold
import com.disfluency.screens.patient.exercises.ExercisePracticeList
import com.disfluency.viewmodel.ExercisesViewModel

@Composable
fun ExerciseAssignmentDetailScreen(assignmentId: String, navController: NavHostController, viewModel: ExercisesViewModel){
    val assignment = remember { mutableStateOf<ExerciseAssignment?>(null) }

    LaunchedEffect(Unit){
        assignment.value = viewModel.getAssignmentById(assignmentId)
    }

    BackNavigationScaffold(
        title = stringResource(R.string.assigned_exercise),
        navController = navController
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).verticalScroll(rememberScrollState())) {
            assignment.value?.let {
                ExerciseDetailScreen(exercise = it.exercise)

                ExercisePracticeList(
                    assignment = it,
                    title = "Resoluciones",
                    emptyListContent = {
                        ImageMessagePage(
                            imageResource = R.drawable.record_action,
                            imageSize = 80.dp,
                            text = "El usuario no ha resuelto este ejercicio todavia"
                        )
                    }
                )
            }
        }
    }
}
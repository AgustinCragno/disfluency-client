package com.disfluency.screens.patient

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.model.ExerciseAssignment
import com.disfluency.navigation.routing.Route
import com.disfluency.screens.therapist.ExerciseDetailScreen
import com.disfluency.viewmodel.ExercisesViewModel

@Composable
fun ExerciseAssignmentDetailScreen(assignmentId: String, navController: NavHostController, viewModel: ExercisesViewModel){
    val assignment = remember { mutableStateOf<ExerciseAssignment?>(null) }

    LaunchedEffect(Unit){
        assignment.value = viewModel.getAssignmentById(assignmentId)
    }

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        assignment.value?.let {
            ExerciseDetailScreen(exercise = it.exercise)

            ExercisePracticeList(assignment = it, navController = navController)


        }
    }

    PracticeButton(assignmentId = assignmentId, navController = navController)
}

@Composable
private fun ExercisePracticeList(assignment: ExerciseAssignment, navController: NavHostController){
    //TODO: implement
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
package com.disfluency.screens.therapist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.disfluency.model.ExerciseAssignment
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
}

@Composable
private fun ExercisePracticeList(assignment: ExerciseAssignment, navController: NavHostController){
    //TODO: implement
}
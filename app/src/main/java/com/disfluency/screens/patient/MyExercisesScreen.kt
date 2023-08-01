package com.disfluency.screens.patient

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.disfluency.components.skeleton.SkeletonLoader
import com.disfluency.components.skeleton.list.ExerciseAssignmentListSkeleton
import com.disfluency.model.Patient
import com.disfluency.navigation.routing.Route
import com.disfluency.screens.therapist.ExerciseAssignmentList
import com.disfluency.viewmodel.ExercisesViewModel

@Composable
fun MyExercisesScreen(patient: Patient, navController: NavHostController, viewModel: ExercisesViewModel){
    LaunchedEffect(Unit) {
        viewModel.getAssignmentsOfPatient(patientId = patient.id)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        SkeletonLoader(
            state = viewModel.assignments,
            content = {
                viewModel.assignments.value?.let {
                    ExerciseAssignmentList(exerciseAssignments = it, navController = navController, onClickRoute = Route.Patient.ExerciseAssignmentDetail)
                }
            },
            skeleton = {
                ExerciseAssignmentListSkeleton()
            }
        )
    }
}
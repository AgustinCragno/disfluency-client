package com.disfluency.screens.patient

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.disfluency.components.bar.SearchBar
import com.disfluency.components.skeleton.SkeletonLoader
import com.disfluency.components.skeleton.list.ExerciseAssignmentListSkeleton
import com.disfluency.model.Patient
import com.disfluency.navigation.routing.BottomNavigationItem
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.BottomNavigationScaffold
import com.disfluency.screens.therapist.ExerciseAssignmentList
import com.disfluency.viewmodel.ExercisesViewModel

@Composable
fun MyExercisesScreen(patient: Patient, navController: NavHostController, viewModel: ExercisesViewModel){
    LaunchedEffect(Unit) {
        viewModel.getAssignmentsOfPatient(patientId = patient.id)
    }

    val filterQuery = remember { mutableStateOf("") }

    BottomNavigationScaffold(
        bottomNavigationItems = BottomNavigationItem.Patient.items(),
        navController = navController
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchBar(filterQuery = filterQuery)

            SkeletonLoader(
                state = viewModel.assignments,
                content = {
                    viewModel.assignments.value?.let { assignments ->
                        ExerciseAssignmentList(
                            exerciseAssignments = assignments.filter { it.exercise.title.contains(filterQuery.value, true) },
                            navController = navController,
                            onClickRoute = Route.Patient.ExerciseAssignmentDetail
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
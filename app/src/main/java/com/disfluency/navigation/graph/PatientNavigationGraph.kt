package com.disfluency.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.disfluency.model.Patient
import com.disfluency.navigation.routing.BottomNavigationItem
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.AppScaffold
import com.disfluency.screens.patient.*
import com.disfluency.screens.patient.success.RecordingConfirmationScreen
import com.disfluency.viewmodel.ExercisesViewModel
import com.disfluency.viewmodel.RecordExerciseViewModel

@Composable
fun PatientNavigationGraph(patient: Patient){
    val exercisesViewModel: ExercisesViewModel = viewModel()
    val recordViewModel = RecordExerciseViewModel(LocalContext.current, LocalLifecycleOwner.current)

    AppScaffold(bottomNavigationItems = BottomNavigationItem.Patient.items()) { navHostController ->

        NavHost(navController = navHostController, startDestination = Route.Patient.Home.path){
            composable(Route.Patient.Home.path){
                HomePatientScreen(patient = patient)
            }
            composable(Route.Patient.MyExercises.path){
                MyExercisesScreen(patient = patient, navController = navHostController, viewModel = exercisesViewModel)
            }
            composable(Route.Patient.ExerciseAssignmentDetail.path){ backStackEntry ->
                backStackEntry.arguments?.getString("id")?.let {
                    ExerciseAssignmentDetailScreen(assignmentId = it, navController = navHostController, viewModel = exercisesViewModel)
                }
            }
            composable(Route.Patient.ExercisePractice.path){ backStackEntry ->
                backStackEntry.arguments?.getString("id")?.let {
                    RecordExerciseScreen(
                        assignmentId = it,
                        navController = navHostController,
                        exercisesViewModel = exercisesViewModel,
                        recordViewModel = recordViewModel
                    )
                }
            }
            composable(Route.Patient.RecordConfirmation.path){
                RecordingConfirmationScreen(navController = navHostController, viewModel = recordViewModel)
            }
        }
    }
}
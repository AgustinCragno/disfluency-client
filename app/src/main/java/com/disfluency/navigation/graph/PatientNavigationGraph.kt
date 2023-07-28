package com.disfluency.navigation.graph

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.disfluency.R
import com.disfluency.components.icon.ImageMessagePage
import com.disfluency.model.Patient
import com.disfluency.navigation.routing.BottomNavigationItem
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.AppScaffold
import com.disfluency.screens.patient.ExerciseAssignmentDetailScreen
import com.disfluency.screens.patient.HomePatientScreen
import com.disfluency.screens.patient.MyExercisesScreen
import com.disfluency.viewmodel.ExercisesViewModel

@Composable
fun PatientNavigationGraph(patient: Patient){
    val exercisesViewModel: ExercisesViewModel = viewModel()

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
                    //TODO: temp
                    ImageMessagePage(imageResource = R.drawable.speech_bubble, text = "Proximamente")
                }
            }
        }
    }
}
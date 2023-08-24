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
import com.disfluency.screens.patient.RecordExercise
import com.disfluency.screens.patient.success.RecordingConfirmationScreen
import com.disfluency.viewmodel.ExercisesViewModel
import com.disfluency.viewmodel.RecordScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun PatientNavigationGraph(patient: Patient){
    val exercisesViewModel: ExercisesViewModel = viewModel()
    val recordViewModel: RecordScreenViewModel = viewModel()

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
//                    ImageMessagePage(imageResource = R.drawable.speech_bubble, text = "Proximamente")

                    RecordExercise(
                        id = "64d2cc7dc2729f363c2d4c97",
                        onSend = {
                            recordViewModel.uploadRecording("64d2cc7dc2729f363c2d4c97", it)
                        },
                        navController = navHostController,
                        viewModel = recordViewModel
                    )
                }
            }
            composable(Route.Patient.RecordConfirmation.path){
                RecordingConfirmationScreen(navController = navHostController, viewModel = recordViewModel)
            }
        }
    }
}
package com.disfluency.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.disfluency.model.user.Patient
import com.disfluency.navigation.routing.Route
import com.disfluency.screens.patient.*
import com.disfluency.screens.patient.exercises.ExerciseAssignmentDetailScreen
import com.disfluency.screens.patient.exercises.MyExercisesScreen
import com.disfluency.screens.patient.exercises.RecordExerciseScreen
import com.disfluency.screens.patient.forms.FormCompletionScreen
import com.disfluency.screens.patient.forms.MyFormsScreen
import com.disfluency.screens.patient.success.RecordingConfirmationScreen
import com.disfluency.viewmodel.ExercisesViewModel
import com.disfluency.viewmodel.FormsViewModel
import com.disfluency.viewmodel.LoggedUserViewModel
import com.disfluency.viewmodel.RecordExerciseViewModel

@Composable
fun PatientNavigationGraph(patient: Patient, loggedUserViewModel: LoggedUserViewModel){
    val exercisesViewModel: ExercisesViewModel = viewModel()
    val formsViewModel: FormsViewModel = viewModel()
    val recordViewModel = RecordExerciseViewModel(LocalContext.current, LocalLifecycleOwner.current)

    val navHostController = rememberNavController()

    NavHost(navController = navHostController, startDestination = Route.Patient.Home.path){
        composable(Route.Patient.Home.path){
            HomePatientScreen(patient = patient, navController = navHostController, viewModel = loggedUserViewModel)
        }
        composable(Route.Patient.MyExercises.path){
            MyExercisesScreen(patient = patient, navController = navHostController, viewModel = exercisesViewModel)
        }
        composable(Route.Patient.MyForms.path){
            MyFormsScreen(patient = patient, navController = navHostController, viewModel = formsViewModel)
        }
        composable(Route.Patient.ExerciseAssignmentDetail.path){ backStackEntry ->
            backStackEntry.arguments?.getString("id")?.let {
                ExerciseAssignmentDetailScreen(assignmentId = it, navController = navHostController, viewModel = exercisesViewModel)
            }
        }
        composable(Route.Patient.FormCompletion.path){ backStackEntry ->
            backStackEntry.arguments?.getString("id")?.let {
                FormCompletionScreen(assignmentId = it, navController = navHostController, viewModel = formsViewModel)
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
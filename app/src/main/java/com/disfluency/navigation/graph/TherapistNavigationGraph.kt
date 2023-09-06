package com.disfluency.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.disfluency.R
import com.disfluency.components.icon.ImageMessagePage
import com.disfluency.model.user.Therapist
import com.disfluency.navigation.routing.Route
import com.disfluency.screens.therapist.exercises.MyExercisesScreen
import com.disfluency.screens.therapist.*
import com.disfluency.screens.therapist.analysis.AnalysisResultsScreen
import com.disfluency.screens.therapist.analysis.AnalysisTranscriptionScreen
import com.disfluency.screens.therapist.analysis.PatientSessionsScreen
import com.disfluency.screens.therapist.analysis.RecordSessionScreen
import com.disfluency.screens.therapist.exercises.ExerciseAssignmentDetailScreen
import com.disfluency.screens.therapist.exercises.PatientExerciseAssignmentsScreen
import com.disfluency.screens.therapist.forms.MyFormsScreen
import com.disfluency.screens.therapist.forms.PatientFormAssignmentsScreen
import com.disfluency.screens.therapist.patients.MyPatientsScreen
import com.disfluency.screens.therapist.patients.NewPatientScreen
import com.disfluency.screens.therapist.patients.PatientDetailScreen
import com.disfluency.screens.therapist.success.NewPatientConfirmationScreen
import com.disfluency.screens.therapist.success.SessionRecordConfirmationScreen
import com.disfluency.viewmodel.*

@Composable
fun TherapistNavigationGraph(therapist: Therapist, loggedUserViewModel: LoggedUserViewModel){
    val patientsViewModel: PatientsViewModel = viewModel()
    val exercisesViewModel: ExercisesViewModel = viewModel()
    val recordViewModel = RecordSessionViewModel(LocalContext.current, LocalLifecycleOwner.current)
    val analysisViewModel: AnalysisViewModel = viewModel()

    val navHostController = rememberNavController()

    NavHost(navController = navHostController, startDestination = Route.Therapist.Home.path){
        composable(Route.Therapist.Home.path){
            HomeTherapistScreen(therapist = therapist, navController = navHostController, viewModel = loggedUserViewModel)
        }
        composable(Route.Therapist.MyPatients.path){
            MyPatientsScreen(therapist = therapist, navController = navHostController, viewModel = patientsViewModel)
        }
        composable(Route.Therapist.MyExercises.path){
            MyExercisesScreen(therapist = therapist, navController = navHostController)
        }
        composable(Route.Therapist.MyForms.path){
            MyFormsScreen(therapist = therapist, navController = navHostController)
        }
        composable(Route.Therapist.NewPatient.path){
            NewPatientScreen(therapist = therapist, navController = navHostController, viewModel = patientsViewModel)
        }
        composable(Route.Therapist.PatientDetail.path, listOf(navArgument("id"){})){ backStackEntry ->
            backStackEntry.arguments?.getString("id")?.let {
                PatientDetailScreen(patientId = it, navController = navHostController, viewModel = patientsViewModel)
            }
        }
        composable(Route.Therapist.PatientExercises.path, listOf(navArgument("id"){})){ backStackEntry ->
            backStackEntry.arguments?.getString("id")?.let {
                PatientExerciseAssignmentsScreen(patientId = it, navController = navHostController, viewModel = exercisesViewModel)
            }
        }
        composable(Route.Therapist.ExerciseAssignmentDetail.path, listOf(navArgument("id"){})){ backStackEntry ->
            backStackEntry.arguments?.getString("id")?.let {
                ExerciseAssignmentDetailScreen(
                    assignmentId = it,
                    navController = navHostController,
                    viewModel = exercisesViewModel
                )
            }
        }
        composable(Route.Therapist.PatientForms.path, listOf(navArgument("id"){})){ backStackEntry ->
            backStackEntry.arguments?.getString("id")?.let {
                PatientFormAssignmentsScreen(patientId = it, navController = navHostController)
            }
        }
        composable(Route.Therapist.ConfirmationNewPatient.path){
            NewPatientConfirmationScreen(navController = navHostController, viewModel = patientsViewModel)
        }
        composable(Route.Therapist.PatientSessions.path, listOf(navArgument("id"){})){ backStackEntry ->
            backStackEntry.arguments?.getString("id")?.let {
                PatientSessionsScreen(
                    patientId = it,
                    navController = navHostController,
                    viewModel = analysisViewModel,
                    recordSessionViewModel = recordViewModel
                )
            }
        }

        composable(Route.Therapist.NewSession.path, listOf(navArgument("id"){})){ backStackEntry ->
            backStackEntry.arguments?.getString("id")?.let {
                RecordSessionScreen(
                    patientId = it,
                    navController = navHostController,
                    viewModel = analysisViewModel,
                    recordViewModel = recordViewModel
                )
            }
        }

        composable(Route.Therapist.NewSessionConfirmation.path, listOf(navArgument("id"){})){ backStackEntry ->
            backStackEntry.arguments?.getString("id")?.let {
                SessionRecordConfirmationScreen(patientId = it, navController = navHostController)
            }
        }

        composable(Route.Therapist.AnalysisTranscription.path, listOf(navArgument("id"){})){ backStackEntry ->
            backStackEntry.arguments?.getString("id")?.let {
                AnalysisTranscriptionScreen(
                    analysisId = it,
                    navController = navHostController,
                    viewModel = analysisViewModel
                )
            }
        }
        composable(Route.Therapist.AnalysisResults.path, listOf(navArgument("id"){})){ backStackEntry ->
            backStackEntry.arguments?.getString("id")?.let {
                AnalysisResultsScreen(
                    analysisId = it,
                    navController = navHostController,
                    viewModel = analysisViewModel
                )
            }
        }
    }
}
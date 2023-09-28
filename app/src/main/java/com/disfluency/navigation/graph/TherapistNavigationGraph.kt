package com.disfluency.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.disfluency.model.user.Therapist
import com.disfluency.navigation.routing.Route
import com.disfluency.screens.therapist.*
import com.disfluency.screens.therapist.analysis.AnalysisResultsScreen
import com.disfluency.screens.therapist.analysis.AnalysisTranscriptionScreen
import com.disfluency.screens.therapist.analysis.PatientSessionsScreen
import com.disfluency.screens.therapist.analysis.RecordSessionScreen
import com.disfluency.screens.therapist.exercises.*
import com.disfluency.screens.therapist.forms.*
import com.disfluency.screens.therapist.patients.MyPatientsScreen
import com.disfluency.screens.therapist.patients.NewPatientScreen
import com.disfluency.screens.therapist.patients.PatientDetailScreen
import com.disfluency.screens.therapist.success.ExerciseAssignmentConfirmationScreen
import com.disfluency.screens.therapist.success.NewExerciseConfirmationScreen
import com.disfluency.screens.therapist.success.NewPatientConfirmationScreen
import com.disfluency.viewmodel.ExercisesViewModel
import com.disfluency.viewmodel.FormsViewModel
import com.disfluency.viewmodel.LoggedUserViewModel
import com.disfluency.viewmodel.PatientsViewModel
import com.disfluency.screens.therapist.success.SessionRecordConfirmationScreen
import com.disfluency.viewmodel.*
import com.disfluency.viewmodel.record.RecordExerciseExampleViewModel
import com.disfluency.viewmodel.record.RecordSessionViewModel

@Composable
fun TherapistNavigationGraph(therapist: Therapist, loggedUserViewModel: LoggedUserViewModel){
    val patientsViewModel: PatientsViewModel = viewModel()
    val exercisesViewModel: ExercisesViewModel = viewModel()
    val formsViewModel: FormsViewModel = viewModel()
    val recordViewModel = RecordSessionViewModel(LocalContext.current, LocalLifecycleOwner.current)
    val analysisViewModel: AnalysisViewModel = viewModel()
    val recordExerciseExampleViewModel = RecordExerciseExampleViewModel(LocalContext.current, LocalLifecycleOwner.current)
    val assignmentsViewModel: AssignmentsViewModel = viewModel()

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
                PatientFormAssignmentsScreen(
                    patientId = it,
                    navController = navHostController,
                    viewModel = formsViewModel
                )
            }
        }
        composable(Route.Therapist.FormAssignmentDetail.path, listOf(navArgument("id"){})){ backStackEntry ->
            backStackEntry.arguments?.getString("id")?.let {
                PatientFormAssignmentResponseScreen(
                    assignmentId = it,
                    navController = navHostController,
                    viewModel = formsViewModel
                )
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
        composable(Route.Therapist.ExerciseDetail.path, listOf(navArgument("id"){})){ backStackEntry ->
            backStackEntry.arguments?.getString("id")?.let {
                ExerciseDetailScreen(
                    exerciseId = it,
                    therapist = therapist,
                    navController = navHostController,
                    viewModel = patientsViewModel,
                    assignmentsViewModel = assignmentsViewModel
                )
            }
        }
        composable(Route.Therapist.FormDetail.path, listOf(navArgument("id"){})){ backStackEntry ->
            backStackEntry.arguments?.getString("id")?.let {
                FormDetailScreen(
                    formId = it,
                    therapist = therapist,
                    navController = navHostController
                )
            }
        }
        composable(Route.Therapist.NewExercise.path){
            ExerciseCreationScreen(
                therapist = therapist,
                navController = navHostController,
                viewModel = exercisesViewModel,
                recordViewModel = recordExerciseExampleViewModel
            )
        }
        composable(Route.Therapist.ConfirmationNewExercise.path){
            NewExerciseConfirmationScreen(
                therapist = therapist,
                navController = navHostController,
                recordViewModel = recordExerciseExampleViewModel
            )
        }
        composable(Route.Therapist.ExerciseAssignmentConfirmation.path){
            ExerciseAssignmentConfirmationScreen(
                navController = navHostController,
                viewModel = assignmentsViewModel
            )
        }
    }
}
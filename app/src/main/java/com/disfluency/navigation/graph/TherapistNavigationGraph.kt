package com.disfluency.navigation.graph

import androidx.compose.runtime.Composable
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
import com.disfluency.screens.therapist.exercises.ExerciseAssignmentDetailScreen
import com.disfluency.screens.therapist.exercises.PatientExerciseAssignmentsScreen
import com.disfluency.screens.therapist.forms.MyFormsScreen
import com.disfluency.screens.therapist.forms.PatientFormAssignmentResponseScreen
import com.disfluency.screens.therapist.forms.PatientFormAssignmentsScreen
import com.disfluency.screens.therapist.patients.MyPatientsScreen
import com.disfluency.screens.therapist.patients.NewPatientScreen
import com.disfluency.screens.therapist.patients.PatientDetailScreen
import com.disfluency.screens.therapist.success.NewPatientConfirmationScreen
import com.disfluency.viewmodel.ExercisesViewModel
import com.disfluency.viewmodel.FormsViewModel
import com.disfluency.viewmodel.LoggedUserViewModel
import com.disfluency.viewmodel.PatientsViewModel

@Composable
fun TherapistNavigationGraph(therapist: Therapist, loggedUserViewModel: LoggedUserViewModel){
    val patientsViewModel: PatientsViewModel = viewModel()
    val exercisesViewModel: ExercisesViewModel = viewModel()
    val formsViewModel: FormsViewModel = viewModel()

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
        composable(Route.Therapist.PatientSessions.path, listOf(navArgument("id"){})){ backStackEntry ->
            backStackEntry.arguments?.getString("id")?.let {
                ImageMessagePage(imageResource = R.drawable.record_action, text = stringResource(id = R.string.patient_has_no_recorded_sessions))
            }
        }
        composable(Route.Therapist.ConfirmationNewPatient.path){
            NewPatientConfirmationScreen(navController = navHostController, viewModel = patientsViewModel)
        }
    }
}
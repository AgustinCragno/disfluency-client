package com.disfluency.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.disfluency.R
import com.disfluency.components.icon.ImageMessagePage
import com.disfluency.model.Therapist
import com.disfluency.navigation.routing.BottomNavigationItem
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.BottomNavigationScaffold
import com.disfluency.screens.therapist.*
import com.disfluency.screens.therapist.success.NewPatientConfirmationScreen
import com.disfluency.viewmodel.ExercisesViewModel
import com.disfluency.viewmodel.PatientsViewModel

@Composable
fun TherapistNavigationGraph(therapist: Therapist){
    val patientsViewModel: PatientsViewModel = viewModel()
    val exercisesViewModel: ExercisesViewModel = viewModel()

    BottomNavigationScaffold(bottomNavigationItems = BottomNavigationItem.Therapist.items()) { navHostController ->

        NavHost(navController = navHostController, startDestination = Route.Therapist.Home.path){
            composable(Route.Therapist.Home.path){
                HomeTherapistScreen(therapist = therapist)
            }
            composable(Route.Therapist.MyPatients.path){
                MyPatientsScreen(therapist = therapist, navController = navHostController, viewModel = patientsViewModel)
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
                        viewModel = exercisesViewModel
                    )
                }
            }
            composable(Route.Therapist.PatientForms.path, listOf(navArgument("id"){})){ backStackEntry ->
                backStackEntry.arguments?.getString("id")?.let {
                    ImageMessagePage(imageResource = R.drawable.form_fill, text = stringResource(id = R.string.patient_has_no_assigned_forms))
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
}
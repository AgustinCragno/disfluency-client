package com.disfluency.navigation.graph

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.disfluency.model.Therapist
import com.disfluency.navigation.routing.BottomNavigationItem
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.AppScaffold
import com.disfluency.screens.therapist.HomeTherapistScreen
import com.disfluency.screens.therapist.MyPatientsScreen
import com.disfluency.screens.therapist.NewPatientScreen
import com.disfluency.screens.therapist.PatientDetailScreen
import com.disfluency.screens.therapist.success.NewPatientConfirmationScreen
import com.disfluency.viewmodel.PatientsViewModel

@Composable
fun TherapistNavigationGraph(therapist: Therapist){
    val patientsViewModel: PatientsViewModel = viewModel()

    AppScaffold(bottomNavigationItems = BottomNavigationItem.Therapist.items()) { navHostController ->

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

                }
            }
            composable(Route.Therapist.PatientForms.path, listOf(navArgument("id"){})){ backStackEntry ->
                backStackEntry.arguments?.getString("id")?.let {

                }
            }
            composable(Route.Therapist.PatientSessions.path, listOf(navArgument("id"){})){ backStackEntry ->
                backStackEntry.arguments?.getString("id")?.let {

                }
            }
            composable(Route.Therapist.ConfirmationNewPatient.path){
                NewPatientConfirmationScreen(navController = navHostController, viewModel = patientsViewModel)
            }
        }
    }
}
package com.disfluency.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.disfluency.model.Patient
import com.disfluency.model.Therapist
import com.disfluency.navigation.graph.PatientNavigationGraph
import com.disfluency.navigation.graph.TherapistNavigationGraph
import com.disfluency.navigation.routing.Route
import com.disfluency.screens.LoginScreen
import com.disfluency.viewmodel.LoggedUserViewModel

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    val userViewModel: LoggedUserViewModel = viewModel()

    NavHost(navController = navController, startDestination = Route.Login.path){
        composable(Route.Login.path){
            LoginScreen(navController, userViewModel)
        }
        composable(Route.Patient.Home.path){
            PatientNavigationGraph(patient = userViewModel.getLoggedUser().role as Patient)
        }
        composable(Route.Therapist.Home.path){
            TherapistNavigationGraph(therapist = userViewModel.getLoggedUser().role as Therapist)
        }
    }
}
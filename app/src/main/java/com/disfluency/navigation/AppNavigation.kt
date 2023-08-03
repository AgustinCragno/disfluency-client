package com.disfluency.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import com.disfluency.model.Patient
import com.disfluency.model.Therapist
import com.disfluency.navigation.graph.PatientNavigationGraph
import com.disfluency.navigation.graph.TherapistNavigationGraph
import com.disfluency.navigation.routing.Route
import com.disfluency.screens.login.*
import com.disfluency.viewmodel.LoggedUserViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation(){
    val navController = rememberAnimatedNavController()
    val userViewModel: LoggedUserViewModel = viewModel()

    AnimatedNavHost(
        navController = navController,
        startDestination = Route.Launch.path,
        enterTransition = { fadeIn(tween(250)) },
        exitTransition = { fadeOut(tween(150)) }
    ){
        composable(
            route = Route.Launch.path,
            enterTransition = {
                if (this.initialState.destination.route.equals(Route.Login.path)) EnterTransition.None
                else fadeIn(tween(250))
            }
        ){
            DisfluencyLaunchScreen(navController, userViewModel)
        }
        composable(
            route = Route.Login.path,
            enterTransition = { EnterTransition.None }
        ){
            LoginScreen(navController, userViewModel)
        }
        composable(Route.SignUpLobby.path){
            SignUpLobbyScreen(navController)
        }
        composable(Route.SignUpPatient.path){
            SignUpPatientOnBoardingScreen(navController)
        }
        composable(Route.SignUpTherapist.path){
            TherapistSignUpScreen(navController, userViewModel)
        }
        composable(Route.Patient.Home.path){
            PatientNavigationGraph(patient = userViewModel.getLoggedUser().role as Patient)
        }
        composable(Route.Therapist.Home.path){
            TherapistNavigationGraph(therapist = userViewModel.getLoggedUser().role as Therapist)
        }
    }
}
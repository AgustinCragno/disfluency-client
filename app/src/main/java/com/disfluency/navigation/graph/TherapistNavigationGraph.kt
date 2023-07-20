package com.disfluency.navigation.graph

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.disfluency.model.Therapist
import com.disfluency.navigation.routing.BottomNavigationItem
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.AppScaffold
import com.disfluency.screens.therapist.HomeTherapistScreen
import com.disfluency.screens.therapist.MyPatientsScreen

@Composable
fun TherapistNavigationGraph(therapist: Therapist){
    AppScaffold(bottomNavigationItems = BottomNavigationItem.Therapist.items()) { navHostController ->

        NavHost(navController = navHostController, startDestination = Route.Therapist.Home.path){
            composable(Route.Therapist.Home.path){
                HomeTherapistScreen(therapist = therapist)
            }
            composable(Route.Therapist.MyPatients.path){
                MyPatientsScreen(therapist = therapist, navController = navHostController)
            }
            composable(Route.Therapist.NewPatient.path){

            }
            composable(Route.Therapist.PatientDetail.path, listOf(navArgument("id"){})){ backStackEntry ->
                backStackEntry.arguments?.getString("id")?.let {

                }
            }
        }
    }
}
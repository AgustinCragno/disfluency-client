package com.disfluency.navigation.graph

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.disfluency.model.Therapist
import com.disfluency.navigation.routing.BottomNavigationItem
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.AppScaffold
import com.disfluency.screens.therapist.HomeTherapistScreen

@Composable
fun TherapistNavigationGraph(therapist: Therapist){
    AppScaffold(bottomNavigationItems = BottomNavigationItem.Therapist.items()) { navHostController ->

        NavHost(navController = navHostController, startDestination = Route.Therapist.Home.path){
            composable(Route.Therapist.Home.path){
                HomeTherapistScreen(therapist = therapist)
            }
            composable(Route.Therapist.Patients.path){

            }
        }
    }
}
package com.disfluency.navigation.graph

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.disfluency.model.Patient
import com.disfluency.navigation.routing.BottomNavigationItem
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.AppScaffold
import com.disfluency.screens.patient.HomePatientScreen

@Composable
fun PatientNavigationGraph(patient: Patient){
    AppScaffold(bottomNavigationItems = BottomNavigationItem.Patient.items()) { navHostController ->

        NavHost(navController = navHostController, startDestination = Route.Patient.Home.path){
            composable(Route.Patient.Home.path){
                HomePatientScreen(patient = patient)
            }
        }
    }
}
package com.disfluency.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.disfluency.navigation.routing.Route
import com.disfluency.screens.LoginScreen

@Composable
fun AppNavigation(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Route.Login.path){
        composable(Route.Login.path){
            LoginScreen()
        }
    }
}
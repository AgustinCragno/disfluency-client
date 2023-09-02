package com.disfluency.screens.therapist.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.bar.SearchBar
import com.disfluency.components.icon.ImageMessagePage
import com.disfluency.model.user.Therapist
import com.disfluency.navigation.routing.BottomNavigationItem
import com.disfluency.navigation.structure.BottomNavigationScaffold

@Composable
fun MyExercisesScreen(
    therapist: Therapist,
    navController: NavHostController
){
    val filterQuery = rememberSaveable { mutableStateOf("") }

    BottomNavigationScaffold(
        bottomNavigationItems = BottomNavigationItem.Therapist.items(),
        navController = navController
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.onPrimaryContainer
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                SearchBar(filterQuery = filterQuery)

                //TODO: agregar listado de ejercicios default
                ImageMessagePage(imageResource = R.drawable.speech_bubble, text = "No tiene ejercicios cargados en el sistema")
            }
        }
    }
}
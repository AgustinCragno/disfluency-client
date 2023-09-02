package com.disfluency.screens.therapist.forms

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
fun MyFormsScreen(
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

                //TODO: agregar listado de forms default
                ImageMessagePage(imageResource = R.drawable.form_fill, text = "No tiene cuestionarios cargados en el sistema")
            }
        }
    }
}
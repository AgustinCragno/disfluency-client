package com.disfluency.screens.therapist.home

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.bar.HomeTopAppBar
import com.disfluency.model.user.Therapist
import com.disfluency.navigation.routing.BottomNavigationItem
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.BottomNavigationScaffold
import com.disfluency.viewmodel.LoggedUserViewModel

@Composable
fun HomeTherapistScreen(therapist: Therapist, navController: NavHostController, viewModel: LoggedUserViewModel){
    Log.v("Therapist: ", therapist.toString())
    BottomNavigationScaffold(
        bottomNavigationItems = BottomNavigationItem.Therapist.items(),
        navController = navController
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            HomeTopAppBar(navController = navController, viewModel = viewModel)

            HomeScreenContent(therapist = therapist, navController = navController)

        }
    }

}


@Composable
private fun HomeScreenContent(therapist: Therapist, navController: NavHostController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        WelcomeBackCarousel(therapist = therapist)

        Spacer(modifier = Modifier.height(24.dp))

        Shortcuts(navController = navController)

        Spacer(modifier = Modifier.height(24.dp))

        NextPatients(therapist = therapist, navController = navController)

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun Shortcuts(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "Atajos",
            style = MaterialTheme.typography.displayMedium,
            color = Color.Black,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
        ){
            ShortcutButton(modifier = Modifier.weight(10f), title = "Ejercicios", background = R.drawable.session_banner_2) {
                navController.navigate(Route.Therapist.NewExercise.path)
            }

            Spacer(modifier = Modifier.height(8.dp).weight(1f))

            ShortcutButton(modifier = Modifier.weight(10f), title = "Cuestionarios", background = R.drawable.form_banner_2) {
                navController.navigate(Route.Therapist.NewForm.path)
            }
        }
    }
}
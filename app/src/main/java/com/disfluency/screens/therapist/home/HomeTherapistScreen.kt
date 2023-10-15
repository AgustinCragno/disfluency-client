package com.disfluency.screens.therapist

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.bar.HomeTopAppBar
import com.disfluency.model.user.Patient
import com.disfluency.model.user.Therapist
import com.disfluency.navigation.routing.BottomNavigationItem
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.BottomNavigationScaffold
import com.disfluency.screens.therapist.home.NextPatients
import com.disfluency.screens.therapist.home.WelcomeBack
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
        WelcomeBack(therapist = therapist)

        Spacer(modifier = Modifier.height(24.dp))

        Shortcuts(navController = navController)

        Spacer(modifier = Modifier.height(24.dp))

        NextPatients(therapist = therapist, navController = navController)

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
            text = "Crea un nuevo...",
            style = MaterialTheme.typography.displayMedium,
            color = Color.Black,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.Center
        ){
            FloatingActionButton(
                onClick = {
                    navController.navigate(Route.Therapist.NewForm.path)
                },
                modifier = Modifier.padding(16.dp),
                containerColor = MaterialTheme.colorScheme.secondary
            ) {
                Row{
                    Icon(Icons.Filled.Assignment, stringResource(id = R.string.create))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Cuestionario")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            FloatingActionButton(
                onClick = {
                    navController.navigate(Route.Therapist.NewExercise.path)
                },
                modifier = Modifier.padding(16.dp),
                containerColor = MaterialTheme.colorScheme.secondary
            ) {
                Row{
                    Icon(Icons.Filled.RecordVoiceOver, stringResource(id = R.string.create))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Ejercicio")
                }
            }
        }
    }
}
package com.disfluency.screens.therapist.exercises

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.model.user.Therapist
import com.disfluency.navigation.structure.BackNavigationScaffold
import com.disfluency.viewmodel.ExercisesViewModel

@Composable
fun ExerciseCreationScreen(
    therapist: Therapist,
    navController: NavHostController,
    viewModel: ExercisesViewModel
){
    BackNavigationScaffold(
        title = stringResource(R.string.new_exercise),
        navController = navController
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

        }
    }
}

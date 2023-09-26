package com.disfluency.screens.therapist.exercises

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.model.user.Therapist
import com.disfluency.navigation.structure.BackNavigationScaffold

@Composable
fun ExerciseDetailScreen(
    exerciseId: String,
    therapist: Therapist,
    navController: NavHostController
){
    val exercise = therapist.exercises.find { it.id == exerciseId }

    BackNavigationScaffold(
        title = stringResource(R.string.exercise),
        navController = navController
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            exercise?.let {
                ExerciseDetailPanelFixed(exercise = it)
            }
        }
    }
}
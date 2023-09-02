package com.disfluency.screens.therapist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.icon.ImageMessagePage
import com.disfluency.model.ExerciseAssignment
import com.disfluency.navigation.structure.BackNavigationScaffold
import com.disfluency.screens.patient.ExercisePracticeItem
import com.disfluency.screens.patient.ExercisePracticeList
import com.disfluency.viewmodel.ExercisesViewModel

@Composable
fun ExerciseAssignmentDetailScreen(assignmentId: String, navController: NavHostController, viewModel: ExercisesViewModel){
    val assignment = remember { mutableStateOf<ExerciseAssignment?>(null) }

    LaunchedEffect(Unit){
        assignment.value = viewModel.getAssignmentById(assignmentId)
    }

    BackNavigationScaffold(
        title = stringResource(R.string.assigned_exercise),
        navController = navController
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).verticalScroll(rememberScrollState())) {
            assignment.value?.let {
                ExerciseDetailScreen(exercise = it.exercise)

                ExercisePracticeList(
                    assignment = it,
                    title = "Resoluciones",
                    emptyListContent = {
                        ImageMessagePage(
                            imageResource = R.drawable.record_action,
                            imageSize = 80.dp,
                            text = "El usuario no ha resuelto este ejercicio todavia"
                        )
                    }
                )
            }
        }
    }
}
package com.disfluency.screens.therapist.success

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.icon.IconMessage
import com.disfluency.components.success.ConfirmationScreen
import com.disfluency.model.user.Therapist
import com.disfluency.navigation.routing.Route
import com.disfluency.ui.theme.Green40
import com.disfluency.viewmodel.record.RecordExerciseExampleViewModel
import com.disfluency.viewmodel.states.ConfirmationState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun NewExerciseConfirmationScreen(
    therapist: Therapist,
    navController: NavHostController,
    recordViewModel: RecordExerciseExampleViewModel
){
    ConfirmationScreen(
        loadingState = recordViewModel.uploadConfirmationState,
        loadingContent = { LoadingState() },
        successContent = { SuccessState() },
        errorContent = { ErrorState() }
    )

    if (recordViewModel.uploadConfirmationState.value > ConfirmationState.LOADING){
        LaunchedEffect(Unit){
            launch {
                if (recordViewModel.uploadConfirmationState.value == ConfirmationState.SUCCESS){
                    val submittedExercise = recordViewModel.uploadedExercise.value!!
                    // TODO sacar ejercicio agregado por front
//                    therapist.addExercise(submittedExercise)
                    recordViewModel.uploadedExercise.value = null
                }
            }
            delay(2000)
            recordViewModel.uploadConfirmationState.value = ConfirmationState.DONE
            navController.popBackStack()
            navController.navigate(Route.Therapist.Home.path)
        }
    }
}


@Composable
private fun LoadingState(){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.padding(16.dp),
            color = Color.White
        )
        Text(
            text = stringResource(R.string.wait_while_exercise_is_submitted),
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SuccessState(){
    IconMessage(
        imageVector = Icons.Filled.Done,
        color = Green40,
        message = stringResource(R.string.exercise_was_submitted_successfully)
    )
}

@Composable
private fun ErrorState(){
    IconMessage(
        imageVector = Icons.Filled.Close,
        color = Color.Red,
        message = stringResource(R.string.an_error_occurred_while_submitting_exercise)
    )
}
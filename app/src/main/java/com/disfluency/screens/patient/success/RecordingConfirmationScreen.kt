package com.disfluency.screens.patient.success

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.success.ConfirmationScreen
import com.disfluency.navigation.routing.Route
import com.disfluency.ui.theme.Green40
import com.disfluency.viewmodel.record.RecordExerciseAssignmentViewModel
import com.disfluency.viewmodel.states.ConfirmationState
import kotlinx.coroutines.delay

const val ON_SUCCESS_ANIMATION_TIME = 300


@Composable
fun RecordingConfirmationScreen(navController: NavHostController, viewModel: RecordExerciseAssignmentViewModel){
    ConfirmationScreen(
        loadingState = viewModel.uploadConfirmationState,
        loadingContent = { LoadingState() },
        successContent = { SuccessState() },
        errorContent = { ErrorState() }
    )

    if (viewModel.uploadConfirmationState.value > ConfirmationState.LOADING){
        LaunchedEffect(Unit){
            delay(2000)
            viewModel.uploadConfirmationState.value = ConfirmationState.DONE
            navController.popBackStack()
            navController.navigate(Route.Patient.Home.path)
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
            text = stringResource(R.string.wait_while_recording_is_uploaded),
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SuccessState(){
    IconMessage(
        imageVector = Icons.Filled.Done,
        color = Green40,
        message = stringResource(R.string.recording_was_uploaded_successfully)
    )
}

@Composable
private fun ErrorState(){
    IconMessage(
        imageVector = Icons.Filled.Close,
        color = Color.Red,
        message = stringResource(R.string.an_error_occurred_while_uploading_recording)
    )
}

@Composable
private fun IconMessage(imageVector: ImageVector, color: Color, message: String){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .padding(16.dp)
                .background(color, CircleShape)
        ){
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            )
        }

        Text(
            text = message,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}
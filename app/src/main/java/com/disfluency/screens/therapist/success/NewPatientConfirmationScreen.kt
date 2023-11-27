package com.disfluency.screens.therapist.success

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.icon.IconMessage
import com.disfluency.components.success.ConfirmationScreen
import com.disfluency.navigation.routing.Route
import com.disfluency.ui.theme.Green40
import com.disfluency.viewmodel.PatientsViewModel
import com.disfluency.viewmodel.states.ConfirmationState
import kotlinx.coroutines.delay

const val ON_SUCCESS_ANIMATION_TIME = 300

@Composable
fun NewPatientConfirmationScreen(navController: NavHostController, viewModel: PatientsViewModel){
    ConfirmationScreen(
        loadingState = viewModel.creationConfirmationState,
        loadingContent = { LoadingState() },
        successContent = { SuccessState() },
        errorContent = { ErrorState() }
    )

    if (viewModel.creationConfirmationState.value > ConfirmationState.LOADING){
        LaunchedEffect(Unit){
            delay(2000)
            viewModel.creationConfirmationState.value = ConfirmationState.DONE
            navController.popBackStack()
            navController.navigate(Route.Therapist.MyPatients.path)
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
            text = stringResource(R.string.wait_while_the_patient_is_submitted),
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
        message = stringResource(R.string.patient_was_created_successfully)
    )
}

@Composable
private fun ErrorState(){
    IconMessage(
        imageVector = Icons.Filled.Close,
        color = Color.Red,
        message = stringResource(R.string.an_error_occurred_while_submitting_patient)
    )
}
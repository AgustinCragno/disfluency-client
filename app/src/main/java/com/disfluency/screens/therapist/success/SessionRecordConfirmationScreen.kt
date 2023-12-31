package com.disfluency.screens.therapist.success

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.icon.IconMessage
import com.disfluency.components.success.ConfirmationScreen
import com.disfluency.navigation.routing.Route
import com.disfluency.viewmodel.states.ConfirmationState
import kotlinx.coroutines.delay



@Composable
fun SessionRecordConfirmationScreen(patientId: String, navController: NavHostController){
    val confirmationState = remember {
        mutableStateOf(ConfirmationState.LOADING)
    }

    ConfirmationScreen(
        loadingState = confirmationState,
        loadingContent = { LoadingState() },
        successContent = { SuccessState() },
        errorContent = { ErrorState() }
    )

    LaunchedEffect(Unit){
        delay(3000)
        confirmationState.value = ConfirmationState.SUCCESS
        delay(3000)
        navController.popBackStack()
        navController.popBackStack()
        navController.navigate(Route.Therapist.PatientSessions.routeTo(patientId))
    }
}

@Composable
private fun LoadingState(){
    IconMessage(
        imageVector = Icons.Filled.Backup,
        color = Color.Transparent,
        message = stringResource(R.string.session_will_be_uploaded_in_background)
    )
}

@Composable
private fun SuccessState(){
    IconMessage(
        imageVector = Icons.Filled.HistoryToggleOff,
        color = Color.Transparent,
        message = stringResource(R.string.analysis_will_be_available_in_a_few_moments)
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

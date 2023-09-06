package com.disfluency.screens.therapist.success

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.disfluency.components.success.ConfirmationScreen
import com.disfluency.navigation.routing.Route
import com.disfluency.ui.theme.Green40
import com.disfluency.viewmodel.RecordExerciseViewModel
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
        navController.navigate(Route.Therapist.PatientSessions.routeTo(patientId))
    }
}

@Composable
private fun LoadingState(){
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        CircularProgressIndicator(
//            modifier = Modifier.padding(16.dp),
//            color = Color.White
//        )
//        Text(
//            text = "Se subira la grabacion en segundo plano",
//            color = Color.White,
//            style = MaterialTheme.typography.bodyMedium,
//            fontWeight = FontWeight.Bold
//        )
//    }

    IconMessage(
        imageVector = Icons.Filled.Backup,
        color = Color.Transparent,
        message = "En unos momentos estara disponible el analisis sistematico de disfluencias"
    )
}

@Composable
private fun SuccessState(){
    IconMessage(
        imageVector = Icons.Filled.HistoryToggleOff,
        color = Color.Transparent,
        message = "En unos momentos estara disponible el analisis sistematico de disfluencias"
    )
}

@Composable
private fun ErrorState(){
    IconMessage(
        imageVector = Icons.Filled.Close,
        color = Color.Red,
        message = "Ocurrio un error al subir la grabacion"
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
                contentDescription = "Done",
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
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(32.dp)
        )
    }
}
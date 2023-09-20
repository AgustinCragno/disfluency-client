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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.disfluency.components.success.ConfirmationScreen
import com.disfluency.navigation.routing.Route
import com.disfluency.ui.theme.Green40
import com.disfluency.viewmodel.FormsViewModel
import com.disfluency.viewmodel.states.ConfirmationState
import kotlinx.coroutines.delay


@Composable
fun FormResponseConfirmationScreen(
    formAssignmentId: String,
    navController: NavHostController,
    viewModel: FormsViewModel
){
    ConfirmationScreen(
        loadingState = viewModel.completionConfirmationState,
        loadingContent = { LoadingState() },
        successContent = { SuccessState() },
        errorContent = { ErrorState() }
    )

    if (viewModel.completionConfirmationState.value > ConfirmationState.LOADING){
        LaunchedEffect(Unit){
            delay(2000)
            if (viewModel.completionConfirmationState.value == ConfirmationState.SUCCESS){
                viewModel.completionConfirmationState.value = ConfirmationState.DONE
                navController.popBackStack()
                navController.navigate(Route.Patient.FormCompletionLastEntry.routeTo(formAssignmentId))
            } else {
                viewModel.completionConfirmationState.value = ConfirmationState.DONE
                navController.popBackStack()
                navController.navigate(Route.Patient.MyForms.path)
            }
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
            text = "Espere un momento mientras se envia el cuestionario",
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
        message = "Se envio el cuestionario correctamente"
    )
}

@Composable
private fun ErrorState(){
    IconMessage(
        imageVector = Icons.Filled.Close,
        color = Color.Red,
        message = "Ocurrio un error al enviar el cuestionario"
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
            fontWeight = FontWeight.Bold
        )
    }
}
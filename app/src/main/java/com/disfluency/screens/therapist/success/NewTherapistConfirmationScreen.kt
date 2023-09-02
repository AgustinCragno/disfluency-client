package com.disfluency.screens.therapist.success

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.disfluency.components.icon.IconMessage
import com.disfluency.components.success.ConfirmationScreen
import com.disfluency.navigation.routing.Route
import com.disfluency.ui.theme.Green40
import com.disfluency.viewmodel.SignUpViewModel
import com.disfluency.viewmodel.states.ConfirmationState
import kotlinx.coroutines.delay


@Composable
fun NewTherapistConfirmationScreen(navController: NavHostController, viewModel: SignUpViewModel){
    ConfirmationScreen(
        loadingState = viewModel.signupState,
        loadingContent = { LoadingState() },
        successContent = { SuccessState() },
        errorContent = { ErrorState() }
    )

    if (viewModel.signupState.value > ConfirmationState.LOADING){
        LaunchedEffect(Unit){
            delay(2000)
            viewModel.signupState.value = ConfirmationState.DONE

            //TODO: redirigir al inicio de nuevo en caso de que sea error
            navController.navigate(Route.Therapist.Home.path){
                popUpTo(navController.graph.id){
                    inclusive = true
                }
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
            text = "Espere un momento mientras se da de alta al usuario",
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SuccessState(){
    //TODO: se podria mostrar el avatar y el nombre, que los mpuedo sacar dle viewModel
    IconMessage(
        imageVector = Icons.Filled.Done,
        color = Green40,
        message = "Se creo el usuario correctamente"
    )
}

@Composable
private fun ErrorState(){
    IconMessage(
        imageVector = Icons.Filled.Close,
        color = Color.Red,
        message = "Ocurrio un error al dar de alta al usuario"
    )
}
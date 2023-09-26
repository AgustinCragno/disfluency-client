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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.icon.IconMessage
import com.disfluency.components.success.ConfirmationScreen
import com.disfluency.navigation.routing.Route
import com.disfluency.ui.theme.Green40
import com.disfluency.viewmodel.PatientSignUpViewModel
import com.disfluency.viewmodel.states.ConfirmationState
import kotlinx.coroutines.delay

@Composable
fun PatientSignUpConfirmationScreen(
    navController: NavHostController,
    viewModel: PatientSignUpViewModel
){
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

            navController.navigate(Route.Login.path){
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
            text = stringResource(R.string.wait_while_user_is_confirmed),
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SuccessState(){
    //TODO: se podria mostrar el avatar y el nombre, que los mpuedo sacar dle viewModel
    IconMessage(
        imageVector = Icons.Filled.Done,
        color = Green40,
        message = stringResource(R.string.user_was_confirmed_successfully_you_will_be_redirected_to_log_in)
    )
}

@Composable
private fun ErrorState(){
    IconMessage(
        imageVector = Icons.Filled.Close,
        color = Color.Red,
        message = stringResource(R.string.an_error_occurred_while_submitting_user)
    )
}
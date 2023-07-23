package com.disfluency.screens.therapist.success

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.disfluency.navigation.routing.Route
import com.disfluency.viewmodel.PatientsViewModel
import kotlinx.coroutines.delay

const val ON_SUCCESS_ANIMATION_TIME = 300

@Composable
fun NewPatientConfirmationScreen(navController: NavHostController, viewModel: PatientsViewModel){
    val animateVisibility = remember {
        mutableStateOf(true)
    }

    Success(
        loadingState = viewModel.newlyCreatedPatient.value == null,
        animateVisibility = animateVisibility.value,
        message = "Se creo el paciente correctamente"
    )

    viewModel.newlyCreatedPatient.value?.let {
        LaunchedEffect(Unit){
            delay(2000)
            animateVisibility.value = false
            navController.popBackStack()
            navController.navigate(Route.Therapist.MyPatients.path)
        }
    }

    //TODO: que mostramos si hay un error en el alta de paciente?
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun Success(loadingState: Boolean, animateVisibility: Boolean, message: String){

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AnimatedVisibility(
            visible = animateVisibility,
            enter = fadeIn(tween(durationMillis = ON_SUCCESS_ANIMATION_TIME * 4, delayMillis = ON_SUCCESS_ANIMATION_TIME * 2))
                    + scaleIn(tween(durationMillis = ON_SUCCESS_ANIMATION_TIME * 4, delayMillis = ON_SUCCESS_ANIMATION_TIME * 2)),
            exit = fadeOut(tween(ON_SUCCESS_ANIMATION_TIME))
        ) {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.primary){}
        }

        AnimatedVisibility(
            visible = loadingState,
            enter = fadeIn(tween(durationMillis = ON_SUCCESS_ANIMATION_TIME * 4, delayMillis = ON_SUCCESS_ANIMATION_TIME * 2)),
            exit = fadeOut(tween(ON_SUCCESS_ANIMATION_TIME * 2))
        ) {
            CircularProgressIndicator(color = Color.White)
        }

        AnimatedVisibility(
            visible = animateVisibility && !loadingState,
            enter = fadeIn(tween(durationMillis = 1, delayMillis = ON_SUCCESS_ANIMATION_TIME)),
            exit = fadeOut(tween(ON_SUCCESS_ANIMATION_TIME))
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Done",
                    tint = Color.White,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(16.dp)
                )
                Text(
                    text = message,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
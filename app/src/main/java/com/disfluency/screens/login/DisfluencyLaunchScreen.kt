package com.disfluency.screens.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.disfluency.R
import com.disfluency.api.session.SessionManager
import com.disfluency.components.animation.DisfluencyAnimatedLogoRise
import com.disfluency.model.Patient
import com.disfluency.model.Therapist
import com.disfluency.navigation.routing.Route
import com.disfluency.viewmodel.LoggedUserViewModel
import com.disfluency.viewmodel.states.LoginState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


val LOGO_OFFSET = 120.dp
const val ON_AUTH_EXIT_TIME = 750

@Composable
fun DisfluencyLaunchScreen(
    navController: NavController,
    viewModel: LoggedUserViewModel = viewModel()
){
    LaunchedEffect(Unit){
        launch {
            if (!viewModel.firstLoadDone.value){
                SessionManager.getRefreshToken()?.let {
                    viewModel.login(it)
                }
            }
        }
    }

    if (viewModel.loginState == LoginState.AUTHENTICATED && viewModel.firstLoadDone.value){
        LaunchedEffect(Unit) {
            launch {
                delay((ON_AUTH_EXIT_TIME * 0.5).toLong())
                navController.navigate(
                    when (viewModel.getLoggedUser()) {
                        is Therapist -> Route.Therapist.Home.path
                        is Patient -> Route.Patient.Home.path
                        else -> throw IllegalStateException("The current user role is not valid")
                    }
                )
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()){
        AnimatedVisibility(
            visible = viewModel.firstLoadDone.value && viewModel.loginState < LoginState.AUTHENTICATED,
            enter = fadeIn(animationSpec = tween(delayMillis = 500))
        ) {
            LaunchScreenContent(navController = navController)
        }

        AnimatedVisibility(
            visible = viewModel.loginState < LoginState.AUTHENTICATED  || !viewModel.firstLoadDone.value,
            exit = fadeOut(tween(ON_AUTH_EXIT_TIME))
        ) {
            DisfluencyAnimatedLogoRise(viewModel = viewModel, riseOffset = LOGO_OFFSET)
        }
    }
}

@Composable
private fun LaunchScreenContent(
    navController: NavController
){
    val logInAction = {
        navController.navigate(route = Route.Login.path)
    }

    val signUpAction = {
        navController.navigate(Route.SignUpLobby.path)
    }

    val animateVisibilityContent = remember { mutableStateOf(false) }
    val alpha: Float by animateFloatAsState(
        targetValue = if (animateVisibilityContent.value) 1f else 0f,
        animationSpec = tween(durationMillis = 200, easing = LinearEasing)
    )

    LaunchedEffect(Unit){
        delay(100)
        animateVisibilityContent.value = true
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(LOGO_OFFSET))

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.displayMedium
        )

        Text(
            modifier = Modifier
                .padding(top = 2.dp)
                .alpha(alpha),
            text = stringResource(R.string.disfluency_slogan),
            style = MaterialTheme.typography.labelSmall
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            modifier = Modifier
                .width(250.dp)
                .alpha(alpha),
            onClick = logInAction
        ) {
            Text(text = stringResource(R.string.enter))
        }


        OutlinedButton(
            modifier = Modifier
                .width(250.dp)
                .alpha(alpha),
            onClick = signUpAction
        ) {
            Text(text = stringResource(id = R.string.signup))
        }
    }
}
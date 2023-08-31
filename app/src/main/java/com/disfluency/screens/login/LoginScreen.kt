package com.disfluency.screens.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.animation.DisfluencyLogo
import com.disfluency.components.inputs.text.EmailInput
import com.disfluency.components.inputs.text.NoValidation
import com.disfluency.components.inputs.text.PasswordInput
import com.disfluency.model.Patient
import com.disfluency.model.Therapist
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.BackNavigationScaffold
import com.disfluency.viewmodel.LoggedUserViewModel
import com.disfluency.viewmodel.states.LoginState
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoggedUserViewModel = viewModel()
){
    BackNavigationScaffold(title = R.string.login, navController = navController, onBackNavigation = { navController.navigate(Route.Launch.path) }) {
        Box(modifier = Modifier.fillMaxSize()){
            Column(
                Modifier
                    .fillMaxSize()
                    .offset(y = -LOGO_OFFSET),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                DisfluencyLogo()
            }

            DisfluencyTitle()

            UsernameAndPasswordForm(
                viewModel = viewModel,
                onSubmit = { account, password -> viewModel.login(account, password) }
            )
        }
    }



    if (viewModel.loginState == LoginState.AUTHENTICATED){
        LaunchedEffect(Unit) {
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

@Composable
private fun DisfluencyTitle(){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(bottom = 29.dp),
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.displayMedium
        )
    }
}

@Composable
private fun UsernameAndPasswordForm(
    viewModel: LoggedUserViewModel,
    onSubmit: (String, String) -> Unit
){
    val animateVisibilityContent = remember { mutableStateOf(false) }
    val alpha: Float by animateFloatAsState(
        targetValue = if (animateVisibilityContent.value) 1f else 0f,
        animationSpec = tween(durationMillis = 200, easing = LinearEasing)
    )

    LaunchedEffect(Unit){
        delay(100)
        animateVisibilityContent.value = true
    }

    val username = remember{ mutableStateOf("") }
    val password = remember{ mutableStateOf("") }

    val submitEnabled = remember(username.value, password.value, viewModel.loginState){
        username.value.isNotBlank() && password.value.isNotBlank() && viewModel.loginState < LoginState.SUBMITTED
    }

    val submitAction = { onSubmit(username.value, password.value) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .offset(y = LOGO_OFFSET + 16.dp)
            .alpha(alpha),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        EmailInput(
            email = username,
            enabled = viewModel.loginState < LoginState.SUBMITTED,
            validation = NoValidation()
        )

        PasswordInput(
            password = password,
            labelId = R.string.password,
            enabled = viewModel.loginState < LoginState.SUBMITTED,
            onSubmit = submitAction,
            validation = NoValidation(),
            validationFailMessage = -1
        )

        Button(
            modifier = Modifier
                .width(250.dp)
                .padding(vertical = 16.dp),
            onClick = submitAction,
            enabled = submitEnabled
        ) {
            Text(stringResource(R.string.enter))
        }

        val errorTextColor = animateColorAsState(targetValue = if (viewModel.loginState == LoginState.NOT_FOUND) MaterialTheme.colorScheme.error else Color.Transparent)
        Text(stringResource(R.string.incorrect_user_or_password), color = errorTextColor.value)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(visible = viewModel.loginState == LoginState.SUBMITTED, enter = fadeIn(), exit = fadeOut()) {
            CircularProgressIndicator()
        }
    }
}
package com.disfluency.screens.login

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.animation.DisfluencyAnimatedLogo
import com.disfluency.components.animation.DisfluencyAnimatedLogoRise
import com.disfluency.components.animation.DisfluencyLogo
import com.disfluency.model.Patient
import com.disfluency.model.Therapist
import com.disfluency.navigation.routing.Route
import com.disfluency.viewmodel.LoggedUserViewModel
import com.disfluency.viewmodel.LoginState
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoggedUserViewModel = viewModel()
){
    SignUpLobbyScaffold(title = R.string.login, navController = navController) {
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

    var username by remember{ mutableStateOf("") }
    var password by remember{ mutableStateOf("") }
    var visiblePassword by remember { mutableStateOf(false) }

    val submitEnabled = remember(username, password, viewModel.loginState){
        username.isNotBlank() && password.isNotBlank() && viewModel.loginState < LoginState.SUBMITTED
    }

    val focusManager = LocalFocusManager.current

    val submitAction = { onSubmit(username, password) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .offset(y = LOGO_OFFSET + 16.dp)
            .alpha(alpha),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        OutlinedTextField(
            modifier = Modifier.padding(8.dp).height(50.dp),
            value = username,
            onValueChange = { username = it },
            placeholder = { Text(stringResource(R.string.username)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            enabled = viewModel.loginState < LoginState.SUBMITTED
        )

        OutlinedTextField(
            modifier = Modifier.padding(8.dp).height(50.dp),
            value = password,
            onValueChange = { password = it },
            placeholder = { Text(stringResource(R.string.password)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Send,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(onSend = {
                focusManager.clearFocus()
                submitAction()
            }),
            singleLine = true,
            visualTransformation = if (visiblePassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { visiblePassword = !visiblePassword }) {
                    if (visiblePassword)
                        Icon(imageVector = Icons.Filled.Visibility, stringResource(id = R.string.hide_password))
                    else
                        Icon(imageVector = Icons.Filled.VisibilityOff, stringResource(id = R.string.show_password))
                }
            },
            enabled = viewModel.loginState < LoginState.SUBMITTED
        )

        Button(
            modifier = Modifier.width(250.dp).padding(8.dp),
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
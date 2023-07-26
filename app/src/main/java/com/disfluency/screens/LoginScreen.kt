package com.disfluency.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
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
import com.disfluency.R
import com.disfluency.components.animation.DisfluencyAnimatedLogoRise
import com.disfluency.model.Patient
import com.disfluency.model.Therapist
import com.disfluency.navigation.routing.Route
import com.disfluency.viewmodel.LoggedUserViewModel
import com.disfluency.viewmodel.LoginState

private val LOGO_OFFSET = 120.dp

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoggedUserViewModel = viewModel()
){
    val animationState = remember { mutableStateOf(!viewModel.firstLogin) }

    Box(modifier = Modifier.fillMaxSize()){
        AnimatedVisibility(visible = animationState.value, enter = fadeIn(animationSpec = tween(delayMillis = 500))) {
            UsernameAndPasswordForm(
                viewModel = viewModel,
                onSubmit = { account, password -> viewModel.login(account, password) }
            )
        }
        DisfluencyAnimatedLogoRise(animationState = animationState, riseOffset = LOGO_OFFSET)
    }

    if (viewModel.loginState == LoginState.AUTHENTICATED){
        LaunchedEffect(Unit) {
            navController.navigate(
                when (viewModel.getLoggedUser().role) {
                    is Therapist -> Route.Therapist.Home.path
                    is Patient -> Route.Patient.Home.path
                    else -> throw IllegalStateException("The current user role is not valid")
                }
            )
        }
    }
}

@Composable
fun UsernameAndPasswordForm(
    viewModel: LoggedUserViewModel,
    onSubmit: (String, String) -> Unit
){
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
            .offset(y = LOGO_OFFSET / 2),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            placeholder = { Text(stringResource(R.string.username)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier.padding(8.dp),
            enabled = viewModel.loginState < LoginState.SUBMITTED
        )

        OutlinedTextField(
            modifier = Modifier.padding(8.dp),
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

        Button(onClick = submitAction, enabled = submitEnabled) {
            Text(stringResource(R.string.login))
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
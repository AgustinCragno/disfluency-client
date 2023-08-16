package com.disfluency.screens.signup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.disfluency.R
import com.disfluency.components.animation.DisfluencyLogo
import com.disfluency.components.inputs.text.EqualToValidation
import com.disfluency.components.inputs.text.PasswordInput
import com.disfluency.components.inputs.text.PasswordValidation
import com.disfluency.components.text.TermsAndConditions
import com.disfluency.ui.theme.DisfluencyTheme
import com.disfluency.utilities.avatar.AvatarManager
import com.disfluency.viewmodel.PatientSignUpViewModel
import kotlinx.coroutines.delay

@Preview
@Composable
fun SignUpPreview() {
    AvatarManager.initialize(LocalContext.current)

    DisfluencyTheme() {
        PatientSignUpScreen(token = "", navController = rememberNavController())
    }
}

@Composable
fun PatientSignUpScreen(
    token: String,
    navController: NavHostController,
    viewModel: PatientSignUpViewModel = viewModel()
) {
    var signUpStep by remember {
        mutableStateOf(1)
    }

    LaunchedEffect(Unit){
        delay(2000)
        viewModel.retrieveUser(token)
    }

    SignUpLobbyScaffold(title = R.string.signup, navController = navController) { paddingValues ->

            AnimatedVisibility(
                visible = viewModel.user.value == null,
                exit = fadeOut(tween(300))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = paddingValues.calculateTopPadding()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            AnimatedVisibility(
                visible = viewModel.user.value != null,
                enter = fadeIn(tween(300, 300))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = paddingValues.calculateTopPadding()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    when(signUpStep){
                        1 -> WelcomePage(viewModel) { signUpStep++ }
                        2 -> PasswordFormPage(viewModel)
                    }
                }
            }
    }
}

@Composable
private fun WelcomePage(viewModel: PatientSignUpViewModel, onNext: () -> Unit){

    DisfluencyLogo()

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        modifier = Modifier
            .width(300.dp)
            .padding(horizontal = 8.dp),
        text = stringResource(id = R.string.welcome),
        style = MaterialTheme.typography.displaySmall,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )

    Text(
        modifier = Modifier
            .width(300.dp)
            .padding(top = 2.dp, start = 8.dp, end = 8.dp),
        text = stringResource(R.string.invitation_message_1),
        style = MaterialTheme.typography.bodyMedium,
        color = Color.Gray,
        textAlign = TextAlign.Center
    )

    Text(
        modifier = Modifier
            .width(300.dp)
            .padding(horizontal = 8.dp, vertical = 16.dp),
        text = viewModel.user.value!!.email,
        style = MaterialTheme.typography.bodyMedium,
        color = Color.DarkGray,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )

    Text(
        modifier = Modifier
            .width(300.dp)
            .padding(start = 8.dp, end = 8.dp),
        text = stringResource(R.string.invitation_message_2),
        style = MaterialTheme.typography.bodyMedium,
        color = Color.Gray,
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(32.dp))

    TextButton(
        onClick = onNext
    ) {
        Text(text = stringResource(id = R.string.next))
    }
}

@Composable
private fun PasswordFormPage(viewModel: PatientSignUpViewModel){
    val passwordRepeat = remember{ mutableStateOf("") }

    val submitEnabled = remember(viewModel.password.value, passwordRepeat.value){
        PasswordValidation().validate(viewModel.password.value)
                && EqualToValidation(viewModel.password.value).validate(passwordRepeat.value)
    }

    Image(
        painter = painterResource(id = viewModel.user.value!!.avatar()),
        contentDescription = "User Thumbnail",
        modifier = Modifier.size(90.dp)
    )

    Text(
        modifier = Modifier
            .width(300.dp)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        text = viewModel.user.value!!.fullName(),
        style = MaterialTheme.typography.displaySmall,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )

    Text(
        modifier = Modifier
            .width(300.dp)
            .padding(top = 2.dp, start = 8.dp, end = 8.dp),
        text = stringResource(R.string.complete_password),
        style = MaterialTheme.typography.bodyMedium,
        color = Color.Gray,
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(16.dp))

    PasswordInput(
        password = viewModel.password,
        labelId = R.string.password,
        enabled = true,
        validation = PasswordValidation(),
        validationFailMessage = R.string.invalid_password
    )

    PasswordInput(
        password = passwordRepeat,
        labelId = R.string.repeat_password,
        enabled = true,
        validation = EqualToValidation(viewModel.password.value),
        validationFailMessage = R.string.password_doesnt_match,
        onSubmit = { viewModel.signUp() }
    )

    Button(
        modifier = Modifier
            .width(250.dp)
            .padding(vertical = 16.dp),
        onClick = { viewModel.signUp() },
        enabled = submitEnabled
    ) {
        Text(stringResource(R.string.agree_and_continue))
    }

    TermsAndConditions()
}
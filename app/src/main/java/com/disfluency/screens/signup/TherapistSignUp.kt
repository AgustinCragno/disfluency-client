package com.disfluency.screens.signup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.dialogs.TermsAndConditionsDialog
import com.disfluency.components.inputs.avatar.AvatarPicker
import com.disfluency.components.inputs.text.*
import com.disfluency.components.text.TermsAndConditions
import com.disfluency.navigation.routing.Route
import com.disfluency.viewmodel.SignUpViewModel
import com.disfluency.viewmodel.states.ConfirmationState

@Composable
fun TherapistSignUpScreen(
    navController: NavHostController,
    viewModel: SignUpViewModel = viewModel()
){
    var signUpStep by remember {
        mutableStateOf(1)
    }

    SignUpLobbyScaffold(title = R.string.signup, navController = navController) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when(signUpStep){
                1 -> UserAndPasswordPage(viewModel) { signUpStep++ }
                2 -> DataAndAvatarPage(viewModel) { signUpStep-- }
            }

        }
    }

    if (viewModel.signupState.value == ConfirmationState.LOADING){
        LaunchedEffect(Unit){
            navController.navigate(Route.ConfirmationNewUser.path)
        }
    }
}

@Composable
private fun UserAndPasswordPage(viewModel: SignUpViewModel, onSubmit: () -> Unit){
    Text(
        text = stringResource(id = R.string.app_name),
        style = MaterialTheme.typography.displayMedium
    )

    Spacer(modifier = Modifier.height(8.dp))

    SignUpForm(viewModel = viewModel, onSubmit = onSubmit)
}

@Composable
private fun SignUpForm(
    viewModel: SignUpViewModel, onSubmit: () -> Unit
){
    val passwordRepeat = remember{ mutableStateOf("") }

    val submitEnabled = remember(viewModel.email.value, viewModel.password.value, passwordRepeat.value){
        EmailValidation().validate(viewModel.email.value)
        && PasswordValidation().validate(viewModel.password.value)
        && EqualToValidation(viewModel.password.value).validate(passwordRepeat.value)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailInput(
            email = viewModel.email,
            enabled = true
        )

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
            onSubmit = onSubmit
        )

        Button(
            modifier = Modifier
                .width(250.dp)
                .padding(vertical = 16.dp),
            onClick = onSubmit,
            enabled = submitEnabled
        ) {
            Text(stringResource(R.string.agree_and_continue))
        }

        TermsAndConditions()
    }
}


@Composable
private fun DataAndAvatarPage(viewModel: SignUpViewModel, onCancel: () -> Unit) {

    val submitEnabled = remember(viewModel.name.value, viewModel.lastName.value){
        MandatoryValidation().validate(viewModel.name.value)
                && MandatoryValidation().validate(viewModel.lastName.value)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .width(300.dp)
                .padding(horizontal = 8.dp),
            text = stringResource(R.string.almost_there),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier
                .width(300.dp)
                .padding(top = 2.dp, start = 8.dp, end = 8.dp),
            text = stringResource(R.string.complete_info_to_enter_disfluency),
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(58.dp))

        AvatarPicker(selectedAvatarIndex = viewModel.avatarIndex)

        Spacer(modifier = Modifier.height(8.dp))

        MandatoryTextInput(state = viewModel.name, label = stringResource(id = R.string.name))
        MandatoryTextInput(state = viewModel.lastName, label = stringResource(id = R.string.last_name))

        Button(
            modifier = Modifier
                .width(250.dp)
                .padding(top = 16.dp),
            onClick = { viewModel.signUp() },
            enabled = submitEnabled
        ) {
            Text(stringResource(R.string.confirm))
        }

        OutlinedButton(
            modifier = Modifier
                .width(250.dp),
            onClick = onCancel
        ) {
            Text(text = stringResource(id = R.string.go_back))
        }
    }
}
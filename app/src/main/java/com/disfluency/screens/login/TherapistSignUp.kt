package com.disfluency.screens.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.dialogs.TermsAndConditionsDialog
import com.disfluency.components.inputs.*
import com.disfluency.viewmodel.LoggedUserViewModel
import com.disfluency.viewmodel.LoginState

@Composable
fun TherapistSignUpScreen(
    navController: NavHostController,
    viewModel: LoggedUserViewModel = viewModel()
){

    //TODO: que esta pantalla sea solo para email y password
    // luego hacemos que en el primer login, lo redirija a una pag para completar nombre
    // y elegir avatar
    // se puede hacer algo similar para el paciente

    SignUpLobbyScaffold(title = R.string.signup, navController = navController) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.displayMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            SignUpForm(
                viewModel = viewModel,
                onSubmit = { email, password -> println("User: $email | Password: $password") }
            )
        }
    }
}


@Composable
private fun SignUpForm(
    viewModel: LoggedUserViewModel,
    onSubmit: (String, String) -> Unit
){
    val email = remember { mutableStateOf("") }
    val password = remember{ mutableStateOf("") }
    val passwordRepeat = remember{ mutableStateOf("") }

    val submitEnabled = remember(email.value, password.value, passwordRepeat.value, viewModel.loginState){
        EmailValidation().validate(email.value)
        && PasswordValidation().validate(password.value)
        && EqualToValidation(password.value).validate(passwordRepeat.value)
        && viewModel.loginState < LoginState.SUBMITTED
    }

    val submitAction = { onSubmit(email.value, password.value) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailInput(
            email = email,
            enabled = viewModel.loginState < LoginState.SUBMITTED
        )

        PasswordInput(
            password = password,
            labelId = R.string.password,
            enabled = viewModel.loginState < LoginState.SUBMITTED,
            validation = PasswordValidation(),
            validationFailMessage = R.string.invalid_password
        )

        PasswordInput(
            password = passwordRepeat,
            labelId = R.string.repeat_password,
            enabled = viewModel.loginState < LoginState.SUBMITTED,
            validation = EqualToValidation(password.value),
            validationFailMessage = R.string.password_doesnt_match,
            onSubmit = submitAction
        )

        Button(
            modifier = Modifier
                .width(250.dp)
                .padding(top = 8.dp),
            onClick = submitAction,
            enabled = submitEnabled
        ) {
            Text(stringResource(R.string.agree_and_continue))
        }

        TermsAndConditions()
    }

    Column(
        modifier = Modifier
            .padding(32.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(visible = viewModel.loginState == LoginState.SUBMITTED, enter = fadeIn(), exit = fadeOut()) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun EmailInput(
    email: MutableState<String>,
    enabled: Boolean
){
    val fontSize = 14.sp

    var wrongValue by remember { mutableStateOf(false) }

    val onValueChange: (String) -> Unit = { it ->
        email.value = it
        wrongValue = !EmailValidation().validate(it)
    }

    val label: @Composable () -> Unit = {
        Text(text = stringResource(R.string.email), fontSize = fontSize)
    }

    OutlinedTextField(
        modifier = Modifier
            .padding(2.dp)
            .height(58.dp),
        value = email.value,
        onValueChange = onValueChange,
        label = label,
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        isError = wrongValue,
        enabled = enabled,
        textStyle = TextStyle(fontSize = fontSize)
    )

    if(wrongValue){
        Text(
            text = stringResource(id = R.string.invalid_email),
            fontSize = 12.sp,
            color = Color.Red
        )
    }
}

//TODO: mover a inputs
@Composable
private fun PasswordInput(
    password: MutableState<String>,
    labelId: Int,
    enabled: Boolean,
    onSubmit: (() -> Unit)? = null,
    validation: InputValidation,
    validationFailMessage: Int
){
    val fontSize = 14.sp

    var wrongValue by remember { mutableStateOf(false) }
    var visiblePassword by remember { mutableStateOf(false) }

    val onValueChange: (String) -> Unit = { it ->
        password.value = it
        wrongValue = !validation.validate(it)
    }

    val label: @Composable () -> Unit = {
        Text(text = stringResource(labelId), fontSize = fontSize)
    }

    val keyboardOptions = KeyboardOptions(
        imeAction = if (onSubmit == null) ImeAction.Next else ImeAction.Send,
        keyboardType = KeyboardType.Password
    )

    val focusManager = LocalFocusManager.current
    val keyboardActions = KeyboardActions(onSend = {
        focusManager.clearFocus()
        onSubmit?.invoke()
    })

    val visualTransformation = if (visiblePassword) VisualTransformation.None else PasswordVisualTransformation()

    val trailingIcon: @Composable () -> Unit = {
        IconButton(onClick = { visiblePassword = !visiblePassword }) {
            if (visiblePassword)
                Icon(imageVector = Icons.Filled.Visibility, stringResource(id = R.string.hide_password))
            else
                Icon(imageVector = Icons.Filled.VisibilityOff, stringResource(id = R.string.show_password))
        }
    }

    OutlinedTextField(
        modifier = Modifier
            .padding(2.dp)
            .height(58.dp),
        value = password.value,
        onValueChange = onValueChange,
        label = label,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        isError = wrongValue,
        enabled = enabled,
        textStyle = TextStyle(fontSize = fontSize)
    )

    if(wrongValue){
        Text(
            text = stringResource(id = validationFailMessage),
            fontSize = 12.sp,
            color = Color.Red
        )
    }
}

@Composable
private fun TermsAndConditions(){
    var openDialog by remember { mutableStateOf(false) }

    val tnc = stringResource(R.string.terms_and_conditions_2)
    val annotatedString = buildAnnotatedString {
        append(stringResource(R.string.terms_and_conditions_1))
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary), ) {
            pushStringAnnotation(tag = tnc, annotation = tnc)
            append(tnc)
        }
    }

    ClickableText(
        modifier = Modifier
            .width(250.dp)
            .padding(vertical = 8.dp),
        text = annotatedString,
        onClick = { offset ->
            annotatedString.getStringAnnotations(offset, offset)
                .firstOrNull()?.let { _ -> openDialog = true }
        },
        style = TextStyle(color = Color.Gray, textAlign = TextAlign.Center)
    )

    if (openDialog){
        TermsAndConditionsDialog {
            openDialog = false
        }
    }
}
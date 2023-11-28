package com.disfluency.components.inputs.text

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.disfluency.R


@Composable
fun EmailInput(
    email: MutableState<String>,
    enabled: Boolean,
    validation: InputValidation = EmailValidation()
){
    val fontSize = 14.sp

    var wrongValue by remember { mutableStateOf(false) }

    val onValueChange: (String) -> Unit = { it ->
        email.value = it
        wrongValue = !validation.validate(it)
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

@Composable
fun PasswordInput(
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
    val keyboardActions = KeyboardActions(
        onSend = {
            focusManager.clearFocus()
            onSubmit?.invoke()
        },
        onNext = {
            focusManager.clearFocus()
        }
    )

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
            color = Color.Red,
            textAlign = TextAlign.Center,
            lineHeight = 10.sp,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
        )
    }
}
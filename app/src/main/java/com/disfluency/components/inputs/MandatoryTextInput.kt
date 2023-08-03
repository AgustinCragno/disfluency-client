package com.disfluency.components.inputs

import android.util.Patterns
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.text.isDigitsOnly
import com.disfluency.R

interface InputValidation {
    fun validate(input: String): Boolean
}

class MandatoryValidation: InputValidation {
    override fun validate(input: String): Boolean {
        return input.isNotEmpty() && input.isNotBlank()
    }
}

class EmailValidation: InputValidation {
    override fun validate(input: String): Boolean {
        return Patterns.EMAIL_ADDRESS.asPredicate().test(input)
    }
}

class DigitsOnlyValidation: InputValidation {
    override fun validate(input: String): Boolean {
        return input.isDigitsOnly()
    }
}

class PasswordValidation: InputValidation {
    override fun validate(input: String): Boolean {
        TODO("Not yet implemented")
        return true
    }
}

@Composable
fun ValidatedTextInput(
    state: MutableState<String>,
    label: String,
    keyboardOptions: KeyboardOptions,
    validations: List<InputValidation>,
    wrongValueMessage: String = "Ingrese un $label válido"
){
    var wrongValue: Boolean by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = state.value,
        onValueChange = {
            state.value = it
            wrongValue = !validations.all { v -> v.validate(it) }
        },
        label = { Text(label) },
        singleLine = true,
        isError = wrongValue,
        trailingIcon = { if (wrongValue) Icon(Icons.Filled.Info, "Error") },
        keyboardOptions = keyboardOptions,
        supportingText = {
            if(wrongValue)
                if(state.value.isBlank()) Text(text = stringResource(id = R.string.required_field))
                else Text(wrongValueMessage)
        }
    )
}

@Composable
fun MandatoryTextInput(state: MutableState<String>, label: String){
    ValidatedTextInput(
        state = state,
        label = label,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, capitalization = KeyboardCapitalization.Words),
        validations = listOf(MandatoryValidation())
    )
}

@Composable
fun MandatoryDigitsInput(state: MutableState<String>, label: String){
    ValidatedTextInput(
        state = state,
        label = label,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.NumberPassword),
        validations = listOf(MandatoryValidation(), DigitsOnlyValidation())
    )
}

@Composable
fun MandatoryEmailInput(state: MutableState<String>, label: String){
    ValidatedTextInput(
        state = state,
        label = label,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
        validations = listOf(MandatoryValidation(), EmailValidation())
    )
}

@Composable
fun MandatoryPasswordInput(state: MutableState<String>, label: String){
    ValidatedTextInput(
        state = state,
        label = label,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, capitalization = KeyboardCapitalization.Words),
        validations = listOf(MandatoryValidation(), PasswordValidation()),
        wrongValueMessage = "Contraseña invalida" //TODO: actualizar a que diga el formato de las pwd
    )
}
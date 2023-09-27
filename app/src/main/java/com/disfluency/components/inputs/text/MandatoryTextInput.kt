package com.disfluency.components.inputs.text

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun MandatoryTextInput(
    modifier: Modifier = Modifier,
    state: MutableState<String>,
    label: String
){
    ValidatedTextInput(
        state = state,
        label = label,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, capitalization = KeyboardCapitalization.Words),
        validations = listOf(MandatoryValidation()),
        modifier = modifier
    )
}

@Composable
fun MandatoryDigitsInput(
    modifier: Modifier = Modifier,
    state: MutableState<String>,
    label: String
){
    ValidatedTextInput(
        state = state,
        label = label,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.NumberPassword),
        validations = listOf(MandatoryValidation(), DigitsOnlyValidation()),
        modifier = modifier
    )
}

@Composable
fun MandatoryEmailInput(
    modifier: Modifier = Modifier,
    state: MutableState<String>,
    label: String
){
    ValidatedTextInput(
        state = state,
        label = label,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
        validations = listOf(MandatoryValidation(), EmailValidation()),
        modifier = modifier
    )
}
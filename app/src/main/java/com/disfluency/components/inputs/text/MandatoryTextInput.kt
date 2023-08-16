package com.disfluency.components.inputs.text

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType

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
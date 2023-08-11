package com.disfluency.components.inputs.text

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.disfluency.R

@Composable
fun ValidatedTextInput(
    state: MutableState<String>,
    label: String,
    keyboardOptions: KeyboardOptions,
    validations: List<InputValidation>,
    wrongValueMessage: String = "Ingrese un $label válido"
){
    val fontSize = 14.sp

    var wrongValue: Boolean by remember { mutableStateOf(false) }

    val onValueChange: (String) -> Unit = { it ->
        state.value = it
        wrongValue = !validations.all { v -> v.validate(it) }
    }

    val labelText: @Composable () -> Unit = {
        Text(text = label, fontSize = fontSize)
    }

    val trailingIcon: @Composable () -> Unit = {
        if (wrongValue) Icon(Icons.Filled.Info, "Error")
    }

    OutlinedTextField(
        modifier = Modifier
            .padding(2.dp)
            .height(58.dp),
        value = state.value,
        onValueChange = onValueChange,
        label = labelText,
        singleLine = true,
        keyboardOptions = keyboardOptions,
        isError = wrongValue,
        trailingIcon = trailingIcon,
        textStyle = TextStyle(fontSize = fontSize)
    )

    if(wrongValue){
        Text(
            text = if(state.value.isBlank()) stringResource(id = R.string.required_field) else wrongValueMessage,
            fontSize = 12.sp,
            color = Color.Red
        )
    }
}
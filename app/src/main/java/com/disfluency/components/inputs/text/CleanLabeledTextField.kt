package com.disfluency.components.inputs.text

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun CleanLabeledTextField(
    modifier: Modifier = Modifier.fillMaxWidth(),
    input: MutableState<String>,
    label: String,
    placeholder: String,
    enabled: Boolean = true,
    textStyle: TextStyle = TextStyle.Default,
    fontSize: TextUnit = TextUnit.Unspecified,
    keyboardAction: ImeAction = ImeAction.Next,
    inputColor: Color = Color.Black,
    onSubmit: (() -> Unit)? = null
){
    Column {
        Text(
            text = label,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.secondary
        )

        BasicTextField(
            modifier = modifier
                .wrapContentHeight(),
            value = input.value,
            onValueChange = {
                input.value = it
            },
            enabled = enabled,
            textStyle = textStyle.copy(fontSize = fontSize, color = inputColor),
            keyboardOptions = KeyboardOptions(imeAction = keyboardAction, capitalization = KeyboardCapitalization.Sentences),
            keyboardActions =
                if (onSubmit == null) KeyboardActions.Default
                else KeyboardActions {
                    if (input.value.isNotBlank())
                        onSubmit.invoke()
                },
            decorationBox = { innerTextField ->
                if (input.value.isBlank()) Text(text = placeholder, color = Color.LightGray, fontSize = fontSize)
                innerTextField()
            }
        )
    }
}
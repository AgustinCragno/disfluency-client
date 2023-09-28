package com.disfluency.components.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.disfluency.R


@Composable
fun SendAndCancelButtons(
    modifier: Modifier = Modifier,
    sendEnabled: Boolean,
    onCancel: () -> Unit,
    onSend: () -> Unit
){
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(
            modifier = Modifier
                .weight(1f),
            onClick = onCancel,
        ) {
            Text(text = stringResource(id = R.string.cancel))
        }

        TextButton(
            modifier = Modifier
                .weight(1f),
            onClick = onSend,
            enabled = sendEnabled
        ) {
            Text(text = stringResource(id = R.string.send))
        }
    }
}
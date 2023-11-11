package com.disfluency.components.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.disfluency.R

@Composable
fun ExitDialog(onAccept: () -> Unit, onCancel: () -> Unit){
    AnimatedDialog(
        modifier = Modifier.wrapContentSize(),
        dismissAction = onCancel
    ) { dismissAction ->
        Surface(
            modifier = Modifier
                .wrapContentSize()
//                .width(100.dp)
                .padding(64.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 8.dp
                ).wrapContentSize()
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(R.string.logout_confirmation),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )

                Row(
                    modifier = Modifier.fillMaxWidth().align(Alignment.End),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(
                        onClick = dismissAction,
                    ) {
                        Text(text = stringResource(id = R.string.cancel))
                    }

                    TextButton(
                        onClick = onAccept,
                    ) {
                        Text(text = stringResource(id = R.string.exit))
                    }
                }
            }
        }
    }
}
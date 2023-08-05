package com.disfluency.components.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.disfluency.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsAndConditionsDialog(dismiss: () -> Unit){
    AlertDialog(
        onDismissRequest = dismiss
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 86.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)) {
                Text(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    text = stringResource(R.string.terms_and_conditions),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )

                //TODO: animate
                //https://medium.com/bilue/expanding-dialog-in-jetpack-compose-a6be40deab86

                Column(
                    modifier = Modifier.fillMaxSize().weight(10f)
                ) {
                    //TODO: scrollable text with t&c
                }

                TextButton(
                    modifier = Modifier.align(Alignment.End).weight(1f),
                    onClick = dismiss,
                ) {
                    Text(text = stringResource(id = R.string.ok))
                }
            }
        }
    }
}
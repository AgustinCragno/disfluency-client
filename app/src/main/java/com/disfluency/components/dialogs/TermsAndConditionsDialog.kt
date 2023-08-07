package com.disfluency.components.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.disfluency.R

@Composable
fun TermsAndConditionsDialog(dismiss: () -> Unit){
    AnimatedDialog(dismissAction = dismiss) { onDismissAction ->
        TermsAndConditionsContent(onOkClick =  onDismissAction)
    }
}

@Composable
private fun TermsAndConditionsContent(onOkClick: () -> Unit){
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 86.dp, horizontal = 48.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                text = stringResource(R.string.terms_and_conditions),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(10f)
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState()),
                text = stringResource(id = R.string.terms_and_conditions_text),
                color = Color.Gray
            )

            TextButton(
                modifier = Modifier
                    .align(Alignment.End)
                    .weight(1f),
                onClick = onOkClick,
            ) {
                Text(text = stringResource(id = R.string.ok))
            }
        }
    }
}
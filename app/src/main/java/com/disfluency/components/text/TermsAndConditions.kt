package com.disfluency.components.text

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.disfluency.R
import com.disfluency.components.dialogs.TermsAndConditionsDialog

@Composable
fun TermsAndConditions(){
    var openDialog by remember { mutableStateOf(false) }

    val tnc = " " + stringResource(R.string.terms_and_conditions_2)
    val annotatedString = buildAnnotatedString {
        append(stringResource(R.string.terms_and_conditions_1))
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary) ) {
            pushStringAnnotation(tag = tnc, annotation = tnc)
            append(tnc)
        }
    }

    ClickableText(
        modifier = Modifier
            .width(250.dp),
        text = annotatedString,
        onClick = { offset ->
            annotatedString.getStringAnnotations(offset, offset)
                .firstOrNull()?.let { _ -> openDialog = true }
        },
        style = TextStyle(color = Color.Gray, textAlign = TextAlign.Center)
    )

    if (openDialog){
        TermsAndConditionsDialog {
            openDialog = false
        }
    }
}
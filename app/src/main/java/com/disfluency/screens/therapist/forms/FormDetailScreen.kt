package com.disfluency.screens.therapist.forms

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.model.form.Form
import com.disfluency.model.form.FormQuestion
import com.disfluency.model.user.Therapist
import com.disfluency.navigation.structure.BackNavigationScaffold

@Composable
fun FormDetailScreen(
    formId: String,
    therapist: Therapist,
    navController: NavHostController
){
    val form = therapist.forms.find { it.id == formId }

    BackNavigationScaffold(
        title = form?.title ?: stringResource(R.string.form),
        navController = navController
    ) { paddingValues ->
        Column(
            modifier = Modifier
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
        ) {
            form?.let {
                FormDetailPanel(form = it)
            }
        }
    }
}

@Composable
private fun FormDetailPanel(form: Form){
    val fontSize = 14.sp

    Column(
        modifier = Modifier.padding(horizontal = 32.dp)
    ) {
        form.questions.forEachIndexed { index, question ->

            Text(
                text = "${index + 1}. ${question.scaleQuestion}",
                fontSize = fontSize,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            ScaleIndicator(question)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = question.followUpQuestion,
                fontSize = fontSize.times(0.9f),
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ScaleIndicator(question: FormQuestion){
    val scaleFontSize = 13.sp

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = question.minValue,
            fontSize = scaleFontSize,
            modifier = Modifier.padding(end = 8.dp)
        )

        Box(
            modifier = Modifier.weight(10f),
            contentAlignment = Alignment.Center
        ){
            LinearProgressIndicator(
                progress = 0f,
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onPrimaryContainer,
                strokeCap = StrokeCap.Round
            )
        }

        Text(
            text = question.maxValue,
            fontSize = scaleFontSize,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
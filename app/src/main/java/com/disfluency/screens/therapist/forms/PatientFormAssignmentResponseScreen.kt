package com.disfluency.screens.therapist.forms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.disfluency.R
import com.disfluency.components.text.WidthWrappedText
import com.disfluency.model.form.*
import com.disfluency.navigation.structure.BackNavigationScaffold
import com.disfluency.ui.theme.DisfluencyTheme
import com.disfluency.utilities.color.darken
import com.smarttoolfactory.bubble.ArrowAlignment
import com.smarttoolfactory.bubble.bubble
import com.smarttoolfactory.bubble.rememberBubbleState
import java.time.LocalDate

@Preview
@Composable
private fun FormResponsePreview(){
    val navController = rememberNavController()

    DisfluencyTheme {
        PatientFormAssignmentResponseScreen(navController = navController)
    }
}

@Composable
fun PatientFormAssignmentResponseScreen(
    navController: NavHostController
){
    val form = Form(
        id = "123",
        title = "Situaciones en el colegio",
        questions = listOf(
            FormQuestion(
                id = "1",
                scaleQuestion = "Tengo dificultades para comunicarme con mis compañeros",
                followUpQuestion = "¿En que situaciones?",
                minValue = "Siempre",
                maxValue = "Nunca"
            ),
            FormQuestion(
                id = "2",
                scaleQuestion = "Trato de no participar en clase para no tener episodios de tartamudez",
                followUpQuestion = "¿Alguna vez tuviste uno de estos episodios al participar?",
                minValue = "Siempre",
                maxValue = "Nunca"
            ),
            FormQuestion(
                id = "3",
                scaleQuestion = "Puedo dar examen oral sin ningun problema",
                followUpQuestion = "¿Esto afecta tus notas?",
                minValue = "Nunca",
                maxValue = "Siempre"
            ),
            FormQuestion(
                id = "4",
                scaleQuestion = "Cuando tengo una duda, levanto la mano y le pregunto al profesor",
                followUpQuestion = "¿De que otra manera tratas de resolver tu duda?",
                minValue = "Nunca",
                maxValue = "Siempre"
            )
        )
    )

    val entries = listOf(
        FormCompletionEntry(
            id = "1",
            date = LocalDate.now(),
            responses = listOf(
                FormQuestionResponse(
                    id = "1",
                    question = form.questions[0],
                    scaleResponse = 3,
                    followUpResponse = ""
                ),
                FormQuestionResponse(
                    id = "2",
                    question = form.questions[1],
                    scaleResponse = 1,
                    followUpResponse = "No, pero por miedo a que ocurran evito participar"
                ),
                FormQuestionResponse(
                    id = "3",
                    question = form.questions[2],
                    scaleResponse = 3,
                    followUpResponse = "A veces me cuesta un poco pero no afecta mis notas ya que el profesor puede interpretar lo que digo"
                ),
                FormQuestionResponse(
                    id = "4",
                    question = form.questions[3],
                    scaleResponse = 2,
                    followUpResponse = "Le pregunto a algun compañero mas tarde"
                )
            )
        ),
        FormCompletionEntry(
            id = "2",
            date = LocalDate.now(),
            responses = listOf(
                FormQuestionResponse(
                    id = "5",
                    question = form.questions[0],
                    scaleResponse = 5,
                    followUpResponse = null
                ),
                FormQuestionResponse(
                    id = "6",
                    question = form.questions[1],
                    scaleResponse = 4,
                    followUpResponse = "Una vez, pero desde ese entonces nunca mas"
                ),
                FormQuestionResponse(
                    id = "7",
                    question = form.questions[2],
                    scaleResponse = 2,
                    followUpResponse = "Un poco si"
                ),
                FormQuestionResponse(
                    id = "8",
                    question = form.questions[3],
                    scaleResponse = 5,
                    followUpResponse = ""
                )
            )
        )
    )

    val testAssignment = FormAssignment(
        id = "1234",
        form = form,
        date = LocalDate.now(),
        completionEntries = entries
    )

    BackNavigationScaffold(title = stringResource(id = R.string.solved_forms), navController = navController) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            FormAssignmentPanel(formAssignment = testAssignment)
        }
    }
}

@Composable
private fun FormAssignmentPanel(formAssignment: FormAssignment){
    val entry = 0

//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 8.dp),
//        colors = CardDefaults.cardColors(Color.White),
//        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
//    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
//                .padding(horizontal = 16.dp)
        ) {
//            Text(
//                text = formAssignment.form.title,
//                color = Color.Black,
//                fontWeight = FontWeight.Bold,
//                fontSize = 24.sp,
//                textAlign = TextAlign.Center,
//                modifier = Modifier
//                    .background(Color.LightGray.copy(alpha = 0.15f))
//                    .fillMaxWidth()
//                    .padding(16.dp)
//            )
//
//            Divider(
//                modifier = Modifier.fillMaxWidth(),
//                thickness = 3.dp,
//                color = Color.Gray.copy(alpha = 0.3f)
//            )

            Spacer(modifier = Modifier.height(16.dp))

            FormResponsePanel(entry = formAssignment.completionEntries[entry])
        }
//    }
}

@Composable
private fun FormResponsePanel(entry: FormCompletionEntry){
    val fontSize = 14.sp

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 32.dp)
    ) {
        entry.responses.forEachIndexed { index, questionResponse ->

            Text(
                text = "${index + 1}. ${questionResponse.question.scaleQuestion}",
                fontSize = fontSize,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            ScaleIndicator(questionResponse = questionResponse)

            if (!questionResponse.followUpResponse.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(16.dp))

                BubbleLayout(
                    bubbleType = BubbleType.THERAPIST
                ) {
                    WidthWrappedText(
                        text = questionResponse.question.followUpQuestion,
                        fontSize = fontSize.times(0.9f),
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        padding = 8.dp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                BubbleLayout(
                    bubbleType = BubbleType.PATIENT
                ) {
                    WidthWrappedText(
                        text = questionResponse.followUpResponse,
                        fontSize = fontSize.times(1f),
                        color = Color.White,
                        padding = 8.dp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ScaleIndicator(questionResponse: FormQuestionResponse){
    val scaleFontSize = 13.sp

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = questionResponse.question.minValue,
            fontSize = scaleFontSize,
            modifier = Modifier.padding(end = 8.dp)
        )

        Box(
            modifier = Modifier.weight(10f),
            contentAlignment = Alignment.Center
        ){
            val percentage = (questionResponse.scaleResponse.toFloat() - 1F) / 4F

            LinearProgressIndicator(
                progress = percentage,
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onPrimaryContainer,
                strokeCap = StrokeCap.Round
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth(percentage + 0.025f)
                    .align(Alignment.CenterStart)
            ){
                val height = 14.dp

                Box(
                    modifier = Modifier
                        .size(6.dp, height)
                        .clip(RoundedCornerShape(32.dp))
                        .background(MaterialTheme.colorScheme.primary.darken(0.1f))
                        .border(1.dp, Color.White.copy(alpha = 0.5f))
                        .align(Alignment.CenterEnd)
                        .offset(y = -height / 2)
                )
            }

        }


        Text(
            text = questionResponse.question.maxValue,
            fontSize = scaleFontSize,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun BubbleLayout(
    bubbleType: BubbleType,
    content: @Composable () -> Unit
) {
    val bubbleState = rememberBubbleState(
        alignment = bubbleType.arrowAlignment,
        cornerRadius = 8.dp,
        arrowOffsetY = 8.dp
    )

    Box(
        modifier = Modifier.fillMaxWidth(),
    ){
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .align(bubbleType.alignment)
                .bubble(
                    bubbleState = bubbleState,
                    color = bubbleType.color
                ),
        ) {
            content()
        }
    }

}

private enum class BubbleType(
    val arrowAlignment: ArrowAlignment,
    val alignment: Alignment,
    val color: Color
){
    PATIENT(ArrowAlignment.RightTop, Alignment.CenterEnd, Color(0xFF55A5A4)),
    THERAPIST(ArrowAlignment.LeftTop, Alignment.CenterStart, Color(0xFF064F4E))
}
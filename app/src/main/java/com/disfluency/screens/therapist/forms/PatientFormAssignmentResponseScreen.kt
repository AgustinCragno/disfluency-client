package com.disfluency.screens.therapist.forms

import androidx.compose.animation.*
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInBounce
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.disfluency.R
import com.disfluency.components.icon.IconLabeled
import com.disfluency.components.text.WidthWrappedText
import com.disfluency.model.form.*
import com.disfluency.navigation.structure.BackNavigationScaffold
import com.disfluency.ui.theme.DisfluencyTheme
import com.disfluency.utilities.color.darken
import com.disfluency.utilities.color.mix
import com.disfluency.utilities.format.formatLocalDate
import com.disfluency.utilities.format.formatLocalDateAsWords
import com.smarttoolfactory.bubble.ArrowAlignment
import com.smarttoolfactory.bubble.bubble
import com.smarttoolfactory.bubble.rememberBubbleState
import kotlinx.coroutines.delay
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
            date = LocalDate.of(2023, 7, 16),
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

    val assignment = testAssignment

    BackNavigationScaffold(
        title = assignment.form.title,
        navController = navController
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )

            FormAssignmentPanel(formAssignment = assignment)
        }
    }
}

@Composable
private fun EntrySelector(
    entries: List<FormCompletionEntry>,
    selectedIndex: MutableState<Int>
){

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        CountIndicator(count = entries.size)

        Spacer(modifier = Modifier.width(16.dp))

        DateSelector(entries = entries, selectedIndex = selectedIndex)
    }

}

@Composable
private fun CountIndicator(count: Int){
    Card(
        modifier = Modifier.wrapContentWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.mix(color = Color.White)
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        IconLabeled(
            modifier = Modifier
                .wrapContentWidth()
                .padding(8.dp),
            icon = Icons.Outlined.Refresh,
            label = count.toString(),
            iconColor = Color.White,
            labelColor = Color.White
        )
    }
}

//TODO: agregar alguna animacion cada vez que se recompone la vista de las respuestas

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateSelector(
    entries: List<FormCompletionEntry>,
    selectedIndex: MutableState<Int>
){
    var isExpanded by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier
            .width(256.dp)
            .clickable { },
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer//.mix(color = Color.Red)
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = !isExpanded }
        ) {
            DateDisplay(
                date = entries[selectedIndex.value].date,
                trailingIcon = Icons.Default.ArrowDropDown,
                modifier = Modifier
                    .padding(8.dp)
                    .menuAnchor()
            )
            
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
                modifier = Modifier
                    .width(256.dp)
                    .background(MaterialTheme.colorScheme.onPrimaryContainer)
            ) {
                entries.forEachIndexed { i, it ->
                    DropdownMenuItem(
                        modifier = Modifier.height(40.dp),
                        text = {
                            DateDisplay(
                                date = it.date,
                                color = Color.Black
                            )
                        },
                        onClick = {
                            selectedIndex.value = i
                            isExpanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DateDisplay(
    modifier: Modifier = Modifier,
    date: LocalDate,
    color: Color = Color.White,
    trailingIcon: ImageVector? = null
){
    val dateAsText = formatLocalDateAsWords(date, stringResource(id = R.string.locale))

    Row(
        modifier = modifier
            .width(240.dp),
        verticalAlignment = CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.CalendarMonth,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = color
        )
        Text(
            text = dateAsText,
            style = MaterialTheme.typography.labelMedium,
            color = color,
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .height(20.dp)
                .width(184.dp)
                .wrapContentHeight()
        )
        trailingIcon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = color
            )
        }

    }
}

@Composable
private fun FormAssignmentPanel(formAssignment: FormAssignment){
    val entry = remember {
        mutableStateOf(0)
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        EntrySelector(entries = formAssignment.completionEntries, selectedIndex = entry)

        Spacer(modifier = Modifier.height(16.dp))

        FormResponsePanel(entry = formAssignment.completionEntries[entry.value])
    }
}

@Composable
private fun FormResponsePanel(entry: FormCompletionEntry){
    val fontSize = 14.sp

    Column(
        modifier = Modifier.padding(horizontal = 32.dp)
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

@OptIn(ExperimentalAnimationApi::class)
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
        modifier = Modifier.fillMaxWidth()
    ){
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .align(bubbleType.alignment)
                .bubble(
                    bubbleState = bubbleState,
                    color = bubbleType.color
                )
                .animateContentSize(tween(400, easing = EaseIn)),
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
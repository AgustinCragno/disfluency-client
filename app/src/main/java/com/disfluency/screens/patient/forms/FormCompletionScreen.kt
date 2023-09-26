package com.disfluency.screens.patient.forms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.disfluency.api.dto.FormEntryDTO
import com.disfluency.components.slider.CustomSlider
import com.disfluency.components.slider.CustomSliderDefaults
import com.disfluency.components.slider.progress
import com.disfluency.components.slider.track
import com.disfluency.components.stepper.PageStepper
import com.disfluency.components.stepper.PageStepperCompact
import com.disfluency.components.stepper.StepScreen
import com.disfluency.model.form.FormAssignment
import com.disfluency.model.form.FormQuestion
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.BackNavigationScaffold
import com.disfluency.utilities.color.darken
import com.disfluency.utilities.color.lighten
import com.disfluency.viewmodel.FormsViewModel
import com.smarttoolfactory.bubble.ArrowAlignment
import com.smarttoolfactory.bubble.ArrowShape
import com.smarttoolfactory.bubble.bubble
import com.smarttoolfactory.bubble.rememberBubbleState

@Composable
fun FormCompletionScreen(assignmentId: String, navController: NavHostController, viewModel: FormsViewModel){
    val assignment = viewModel.getAssignmentById(assignmentId)

    BackNavigationScaffold(
        title = assignment.form.title,
        navController = navController
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            FormCompletion(assignment = assignment, navController = navController, viewModel = viewModel)
        }
    }


}

@Composable
private fun FormCompletion(
    assignment: FormAssignment,
    navController: NavHostController,
    viewModel: FormsViewModel
){
    var submitted by remember { mutableStateOf(false) }

    val responses = remember { mutableListOf<QuestionResponse>() }

    val steps = remember {
        assignment.form.questions.mapIndexed { index, it ->
            val response = QuestionResponse(it.id)
            responses.add(response)

            StepScreen(
                backButtonText = if (index == 0) "Salir" else "Atras",
                nextButtonText = if (index == assignment.form.questions.size - 1) "Enviar" else "Siguiente"
            ) {
                QuestionPageScreen(questionPage = it, response = response)
            }
        }.toList()
    }

    PageStepperCompact(
        steps = steps,
        onCancel = { navController.popBackStack() },
        onFinish = {
            viewModel.completeFormAssignment(formAssignmentId = assignment.id, responses = FormEntryDTO.from(responses))
            submitted = true
        }
    )

    if (submitted){
        LaunchedEffect(Unit){
            navController.popBackStack()
            navController.navigate(Route.Patient.FormCompletionConfirmation.routeTo(assignment.id))
        }
    }
}

@Composable
fun QuestionPageScreen(questionPage: FormQuestion, response: QuestionResponse) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        val (centerElement, topElement, bottomElement) = createRefs()

        ScaleQuestion(
            questionPage = questionPage,
            modifier = Modifier.constrainAs(topElement){
                centerHorizontallyTo(parent)
                bottom.linkTo(centerElement.top, 16.dp)
            }
        )

        QuestionSlider(
            questionPage = questionPage,
            response = response,
            modifier = Modifier.constrainAs(centerElement){
                centerVerticallyTo(parent)
            }
        )

        FollowUpQuestion(
            questionPage = questionPage,
            response = response,
            modifier = Modifier.constrainAs(bottomElement){
                centerHorizontallyTo(parent)
                top.linkTo(centerElement.bottom, 32.dp)
            }
        )
    }
}

@Composable
private fun FollowUpQuestion(
    questionPage: FormQuestion,
    response: QuestionResponse,
    modifier: Modifier = Modifier
) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = questionPage.followUpQuestion,
            style = MaterialTheme.typography.displayMedium,
            fontSize = 16.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        val bubbleState = rememberBubbleState(
            alignment = ArrowAlignment.BottomRight,
            cornerRadius = 16.dp,
            arrowOffsetY = 8.dp
        )

        val bubbleBackground = MaterialTheme.colorScheme.secondaryContainer.lighten(0.95f)
        val bubbleBorderBackground = MaterialTheme.colorScheme.secondaryContainer
        val bubbleTextColor = MaterialTheme.colorScheme.secondary.darken()

        TextField(
            value = response.followUpResponse.value,
            onValueChange = {
                response.followUpResponse.value = it
            },
            maxLines = 3,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = bubbleBackground,
                focusedContainerColor = bubbleBackground,
                unfocusedTextColor = bubbleTextColor,
                focusedTextColor = bubbleTextColor,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            placeholder = {
                Text(text = "Expanda su respuesta si es necesario", color = Color.LightGray)
            },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .bubble(
                    bubbleState = bubbleState,
                    color = bubbleBackground,
                    borderStroke = BorderStroke(1.dp, bubbleBorderBackground)
                )
        )
    }

}

@Composable
private fun ScaleQuestion(
    questionPage: FormQuestion,
    modifier: Modifier = Modifier
) {
    Text(
        text = questionPage.scaleQuestion,
        style = MaterialTheme.typography.displayMedium,
        fontSize = 20.sp,
        color = Color.Black,
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
    )
}

@Composable
private fun QuestionSlider(
    questionPage: FormQuestion,
    response: QuestionResponse,
    modifier: Modifier = Modifier
){
    Column(modifier) {
        Box(Modifier.fillMaxWidth()) {
            Text(text = questionPage.minValue, modifier = Modifier.align(Alignment.TopStart))
            Text(text = questionPage.maxValue, modifier = Modifier.align(Alignment.TopEnd))
        }

        ScaleSlider(response = response)
    }
}

@Composable
private fun ScaleSlider(response: QuestionResponse){
    CustomSlider(
        modifier = Modifier.fillMaxWidth(),
        value = response.scaleResponse.value,
        onValueChange = { response.scaleResponse.value = it },
        thumb = { thumbValue ->
            CustomSliderDefaults.Thumb(
                thumbValue = "$thumbValue",
                color = MaterialTheme.colorScheme.primary,
                size = 30.dp
            )
        },
        track = { sliderPositions ->
            Box(
                modifier = Modifier
                    .track()
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
                    .background(Color.White),
                contentAlignment = Alignment.CenterStart
            ) {
                Box(
                    modifier = Modifier
                        .progress(sliderPositions = sliderPositions)
                        .background(brush = createStripeBrush())
                )
            }
        }
    )
}

@Composable
private fun createStripeBrush(
    stripeColor: Color = MaterialTheme.colorScheme.primary.lighten(0.9f),
    stripeBg: Color = MaterialTheme.colorScheme.primary.lighten(0.8f),
    stripeWidth: Dp = 5.dp
): Brush {
    val stripeWidthPx = with(LocalDensity.current) { stripeWidth.toPx() }
    val brushSizePx = 2 * stripeWidthPx
    val stripeStart = stripeWidthPx / brushSizePx

    return Brush.linearGradient(
        stripeStart to stripeBg,
        stripeStart to stripeColor,
        start = Offset(0f, 0f),
        end = Offset(brushSizePx, brushSizePx),
        tileMode = TileMode.Repeated
    )
}

class QuestionResponse(val idQuestion: String) {
    val scaleResponse: MutableState<Float> = mutableStateOf(1f)
    val followUpResponse: MutableState<String> = mutableStateOf("")

    override fun toString(): String {
        return scaleResponse.value.toString() + " " + followUpResponse.value + " " + idQuestion
    }
}
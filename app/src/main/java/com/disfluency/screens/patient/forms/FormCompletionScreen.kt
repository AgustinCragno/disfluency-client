package com.disfluency.screens.patient.forms

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.disfluency.api.dto.FormEntryDTO
import com.disfluency.api.dto.QuestionResponseDTO
import com.disfluency.components.stepper.PageStepper
import com.disfluency.components.stepper.StepScreen
import com.disfluency.model.form.FormAssignment
import com.disfluency.model.form.FormQuestion
import com.disfluency.navigation.routing.Route
import com.disfluency.viewmodel.FormsViewModel

@SuppressLint("MutableCollectionMutableState")
@Composable
fun FormCompletionScreen(assignmentId: String, navController: NavHostController, viewModel: FormsViewModel){
    val assignment = remember { mutableStateOf<FormAssignment?>(null) }

    LaunchedEffect(Unit){
        assignment.value = viewModel.getAssignmentById(assignmentId)
    }

    var submitted by remember { mutableStateOf(false) }

    assignment.value?.let {
        val responses = remember { mutableStateOf<MutableList<QuestionResponse>>(mutableListOf()) }
        val steps = remember { mutableStateOf<MutableList<StepScreen>>(mutableListOf()) }
        if (steps.value.size < it.form.questions.size) {
            steps.value.addAll(it.form.questions.map { formQuestion ->
                val response = QuestionResponse(formQuestion.id)
                responses.value.add(response)
                StepScreen("") {
                    QuestionPageScreen(questionPage = formQuestion, response = response)
                }
            })
        }
        Column(Modifier.fillMaxSize()) {
            Text(text = it.form.title, fontSize = 24.sp, modifier = Modifier.padding(16.dp))
            PageStepper(steps = steps.value, onCancel = { navController.popBackStack() }) {
                val newEntry = FormEntryDTO(responses.value.map { r -> QuestionResponseDTO(r) })
                viewModel.completeFormAssignment(assignmentId, newEntry)
                submitted = true
            }
        }
    }

    if (submitted){
        LaunchedEffect(Unit){
            navController.popBackStack()
            navController.navigate(Route.Patient.FormCompletionConfirmation.routeTo(assignmentId))
        }
    }
}

@Composable
fun QuestionPageScreen(questionPage: FormQuestion, response: QuestionResponse) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScaleQuestion(questionPage, response)

        Spacer(modifier = Modifier.height(32.dp))

        FollowUpQuestion(questionPage, response)
    }
}

@Composable
private fun FollowUpQuestion(
    questionPage: FormQuestion,
    response: QuestionResponse
) {
    Text(text = questionPage.followUpQuestion, fontSize = 20.sp)
    TextField(value = response.followUpResponse.value, onValueChange = {
        response.followUpResponse.value = it
    }, keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next))
}

@Composable
private fun ScaleQuestion(
    questionPage: FormQuestion,
    response: QuestionResponse
) {
    Text(text = questionPage.scaleQuestion, fontSize = 20.sp)
    Column {
        Box(Modifier.fillMaxWidth()) {
            Text(text = questionPage.minValue, modifier = Modifier.align(Alignment.TopStart))
            Text(text = questionPage.maxValue, modifier = Modifier.align(Alignment.TopEnd))
        }
        Slider(
            value = response.scaleResponse.value,
            onValueChange = { response.scaleResponse.value = it },
            valueRange = 1f..5f,
            steps = 3
        )
    }
}

class QuestionResponse(val idQuestion: String) {
    val scaleResponse: MutableState<Float> = mutableStateOf(1f)
    val followUpResponse: MutableState<String> = mutableStateOf("")

    override fun toString(): String {
        return scaleResponse.value.toString() + " " + followUpResponse.value + " " + idQuestion
    }
}
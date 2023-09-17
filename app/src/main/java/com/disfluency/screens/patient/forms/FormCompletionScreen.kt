package com.disfluency.screens.patient.forms

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.disfluency.api.dto.FormEntryDTO
import com.disfluency.components.stepper.PageStepper
import com.disfluency.components.stepper.StepScreen
import com.disfluency.model.form.FormAssignment
import com.disfluency.model.form.FormQuestion
import com.disfluency.navigation.routing.Route
import com.disfluency.viewmodel.FormsViewModel
import java.time.LocalDate

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
            PageStepper(steps = steps.value, onCancel = { navController.popBackStack() }) {
                val newEntry = FormEntryDTO(responses.value)
                viewModel.completeFormAssignment(assignmentId, newEntry)
                submitted = true
            }
        }
    }

    if (submitted){
        LaunchedEffect(Unit){
            navController.popBackStack()
            navController.navigate(Route.Patient.MyForms.path)
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
        Text(text = questionPage.scaleQuestion)
        Slider(
            value = response.scaleResponse.value,
            onValueChange = { response.scaleResponse.value = it },
            valueRange = 1f..5f,
            steps = 3
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = questionPage.followUpQuestion)
        TextField(value = response.followUpResponse.value, onValueChange = {
            response.followUpResponse.value = it
        }, keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next))
    }
}

class QuestionResponse(private val idQuestion: String) {
    val scaleResponse: MutableState<Float> = mutableStateOf(1f)
    val followUpResponse: MutableState<String> = mutableStateOf("")

    override fun toString(): String {
        return scaleResponse.value.toString() + " " + followUpResponse.value + " " + idQuestion
    }
}
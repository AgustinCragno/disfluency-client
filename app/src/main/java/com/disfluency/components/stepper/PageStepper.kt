package com.disfluency.components.stepper

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class StepScreen(
    val stepTitle: String = "",
    val forwardEnabled: Boolean = true,
    val backButtonText: String = "Atras",
    val nextButtonText: String = "Siguiente",
    val content: @Composable () -> Unit
)

@Composable
fun PageStepper(steps: List<StepScreen>, onCancel: () -> Unit, onFinish: () -> Unit){
    val currentStep = rememberSaveable { mutableStateOf(1) }

    val stepBack: () -> Unit = {
        if (currentStep.value == 1){
            onCancel()
        }else{
            currentStep.value--
        }
    }

    BackHandler() {
        stepBack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Stepper(
            modifier = Modifier.padding(bottom = 16.dp),
            numberOfSteps = steps.size,
            currentStep = currentStep.value,
            stepDescriptionList = steps.map { it.stepTitle },
            selectedColor = MaterialTheme.colorScheme.primary,
            unSelectedColor= MaterialTheme.colorScheme.onPrimaryContainer
        )

        steps[currentStep.value - 1].content()

        StepperButtons(
            steps = steps,
            currentStep = currentStep,
            stepBack = stepBack,
            onFinish = onFinish
        )
    }
}

@Composable
private fun StepperButtons(
    steps: List<StepScreen>,
    currentStep: MutableState<Int>,
    stepBack: () -> Unit,
    onFinish: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        TextButton(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            onClick = stepBack,
            colors = ButtonDefaults.outlinedButtonColors()
        ) {
            Text(text = steps[currentStep.value - 1].backButtonText)
        }



        if (currentStep.value == steps.size){
            TextButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                onClick = onFinish,
                enabled = steps[currentStep.value - 1].forwardEnabled,
                colors = ButtonDefaults.filledTonalButtonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = Color.White)
            ) {
                Text(text = steps[currentStep.value - 1].nextButtonText)
            }
        }else {
            OutlinedButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                onClick = { currentStep.value++ },
                enabled = steps[currentStep.value - 1].forwardEnabled,
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White, contentColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = steps[currentStep.value - 1].nextButtonText)
            }
        }

    }
}

@Composable
fun PageStepperCompact(
    steps: List<StepScreen>,
    onCancel: () -> Unit,
    onFinish: () -> Unit
){
    val currentStep = rememberSaveable { mutableStateOf(1) }

    val stepBack: () -> Unit = {
        if (currentStep.value == 1){
            onCancel()
        }else{
            currentStep.value--
        }
    }

    BackHandler() {
        stepBack()
    }

    Box(modifier = Modifier.fillMaxSize()){
        CurrentStepIndicator(
            currentStep = currentStep,
            steps = steps,
            modifier = Modifier.align(Alignment.TopEnd)
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.weight(1f)) {
                steps[currentStep.value - 1].content()
            }

            StepperButtons(
                steps = steps,
                currentStep = currentStep,
                stepBack = stepBack,
                onFinish = onFinish
            )
        }
    }
}

@Composable
private fun CurrentStepIndicator(
    currentStep: MutableState<Int>,
    steps: List<StepScreen>,
    modifier: Modifier = Modifier
){
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        Surface(
            modifier = Modifier.wrapContentSize(),
            color = MaterialTheme.colorScheme.secondary,
            tonalElevation = 8.dp,
            shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp, bottomEnd = 0.dp, topEnd = 0.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                val fontSize = 17.sp
                val fontColor = Color.White

                Text(
                    text = "${currentStep.value}",
                    color = fontColor.copy(alpha = if (currentStep.value == steps.size) 1f else 0.75f),
                    fontWeight = FontWeight.Bold,
                    fontSize = fontSize
                )
                Text(
                    text = "/${steps.size}",
                    color = fontColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = fontSize
                )
            }

        }
    }
}
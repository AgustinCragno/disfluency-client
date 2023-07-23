package com.disfluency.components.stepper

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class StepScreen(val stepTitle: String, val forwardEnabled: Boolean = true, val content: @Composable () -> Unit)

@Composable
fun PageStepper(steps: List<StepScreen>, onCancel: () -> Unit, onFinish: () -> Unit){
    var currentStep by rememberSaveable { mutableStateOf(1) }

    val stepBack: () -> Unit = {
        if (currentStep == 1){
            onCancel()
        }else{
            currentStep--
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
            currentStep = currentStep,
            stepDescriptionList = steps.map { it.stepTitle },
            selectedColor = MaterialTheme.colorScheme.primary,
            unSelectedColor= MaterialTheme.colorScheme.onPrimaryContainer
        )

        steps[currentStep - 1].content()

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
                Text(text = "Atras")
            }

            TextButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                onClick = {
                    if (currentStep == steps.size){
                        onFinish()
                    }else{
                        currentStep++
                    }
                },
                enabled = steps[currentStep - 1].forwardEnabled,
                colors = ButtonDefaults.filledTonalButtonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = Color.White)
            ) {
                Text(text = "Siguiente")
            }
        }
    }
}

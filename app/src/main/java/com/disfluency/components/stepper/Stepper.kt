package com.disfluency.components.stepper

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.ExtraBold
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.disfluency.ui.theme.DisfluencyTheme


/**
 * Extracted from 'com.github.maryamrzdh:compose-stepper:1.0.0-beta01'
 * and slightly modified
 */

@Composable
fun Stepper(modifier: Modifier = Modifier,
            numberOfSteps: Int,
            currentStep: Int,
            stepDescriptionList : List<String> = List(numberOfSteps){""},
            unSelectedColor: Color = Color.LightGray,
            selectedColor:Color? = null
) {

    val descriptionList= MutableList(numberOfSteps){""}

    stepDescriptionList.forEachIndexed { index, element ->
        if (index<numberOfSteps)
            descriptionList[index]=element
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (step in 1..numberOfSteps) {
            Step(
                modifier = Modifier.weight(1F),
                step = step,
                isCompete = step < currentStep,
                isCurrent = step == currentStep,
                isComplete= step == numberOfSteps,
                stepDescription= descriptionList[step-1],
                unSelectedColor=unSelectedColor,
                selectedColor = selectedColor,
            )
        }
    }
}

@Composable
private fun Step(modifier: Modifier = Modifier,
                 step :Int,
                 isCompete: Boolean,
                 isCurrent: Boolean,
                 isComplete:Boolean,
                 stepDescription:String,
                 unSelectedColor:Color,
                 selectedColor: Color?,
) {

    val transition = updateTransition(isCompete, label = "")

    val innerCircleColor by transition.animateColor(label = "innerCircleColor") {
        if (it) selectedColor?: MaterialTheme.colorScheme.primary else unSelectedColor
    }
    val txtColor by transition.animateColor(label = "txtColor")
    { if (it || isCurrent) selectedColor?:MaterialTheme.colorScheme.primary else Color.Gray }

    val color by transition.animateColor(label = "color")
    { if (it || isCurrent) selectedColor?:MaterialTheme.colorScheme.primary else unSelectedColor }

    val textSize by remember { mutableStateOf(12.sp) }

    ConstraintLayout(modifier = modifier) {

        val (circle, txt, line) = createRefs()

        Surface(
            shape = CircleShape,
            color = color,
            modifier = Modifier
                .size(25.dp)
                .constrainAs(circle) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
        ) {

            Box(contentAlignment = Alignment.Center) {
                if (isCompete)
                    Icon(
                        imageVector = Icons.Default.Done,"done",
                        modifier=modifier.padding(4.dp),
                        tint = Color.White)
                else
                    Text(
                        text = step.toString(),
                        fontWeight = ExtraBold,
                        color = Color.White,
                        fontSize = 13.sp)
            }
        }

        Text(
            modifier = Modifier.constrainAs(txt) {
                top.linkTo(circle.bottom, margin = 3.dp)
                start.linkTo(circle.start)
                end.linkTo(circle.end)
                bottom.linkTo(parent.bottom)
            },
            fontSize = textSize,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = stepDescription,
            color  = txtColor,
        )

        if (!isComplete) {
            //Line
            Divider(
                modifier = Modifier.constrainAs(line){
                    top.linkTo(circle.top)
                    bottom.linkTo(circle.bottom)
                    start.linkTo(circle.end)
                },
                color = innerCircleColor,
                thickness = 6.dp,
            )
        }
    }
}



@Preview
@Composable
fun CustomStepperPreview(){
    val steps = listOf(
        StepScreen("Avatar"){
        },
        StepScreen("Datos"){
        },
        StepScreen("Turno"){
        },
        StepScreen("Confirmar"){
        }
    )

    DisfluencyTheme {
        PageStepper(steps = steps, onCancel = {  }) {

        }
    }
}
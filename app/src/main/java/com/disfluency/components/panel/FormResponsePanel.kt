package com.disfluency.components.panel

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.disfluency.components.text.WidthWrappedText
import com.disfluency.model.form.FormCompletionEntry
import com.disfluency.model.form.FormQuestion
import com.disfluency.model.form.FormQuestionResponse
import com.disfluency.ui.theme.DisfluencyTheme
import com.disfluency.utilities.color.darken
import com.smarttoolfactory.bubble.ArrowAlignment
import com.smarttoolfactory.bubble.bubble
import com.smarttoolfactory.bubble.rememberBubbleState


@Composable
fun FormResponsePanel(entry: FormCompletionEntry){
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
                        text = questionResponse.question.followUpQuestion.trim(),
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
                        text = questionResponse.followUpResponse.trim(),
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

        val percentage = (questionResponse.scaleResponse.toFloat() - 1F) / 4F

        AnimatedProgressIndicator(percentage = percentage, modifier = Modifier.weight(10f))

        Text(
            text = questionResponse.question.maxValue,
            fontSize = scaleFontSize,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun AnimatedProgressIndicator(modifier: Modifier = Modifier, percentage: Float){
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        var state by remember(percentage) {
            mutableStateOf(false)
        }

        if (!state) {
            state = true
        }

        val animatedPercentage by animateFloatAsState(
            targetValue = if (!state) 0.5f else percentage,
            animationSpec = tween(400, easing = EaseIn)
        )

        LinearProgressIndicator(
            progress = animatedPercentage,
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.onPrimaryContainer,
            strokeCap = StrokeCap.Round
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(animatedPercentage + 0.025f)
                .align(Alignment.CenterStart)
        ) {
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